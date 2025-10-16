package com.eneifour.fantry.auction.service;

import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.auction.domain.SaleStatus;
import com.eneifour.fantry.auction.domain.SaleType;
import com.eneifour.fantry.auction.dto.AuctionDetailResponse;
import com.eneifour.fantry.auction.dto.AuctionRequest;
import com.eneifour.fantry.auction.dto.AuctionSearchCondition; // AuctionSearchCondition 임포트 추가
import com.eneifour.fantry.auction.dto.AuctionSummaryResponse;
import com.eneifour.fantry.auction.exception.AuctionException;
import com.eneifour.fantry.auction.exception.ErrorCode;
import com.eneifour.fantry.auction.exception.MemberException;
import com.eneifour.fantry.auction.repository.AuctionRepository;
import com.eneifour.fantry.auction.repository.AuctionSpecification; // AuctionSpecification 임포트 추가
import com.eneifour.fantry.bid.domain.Bid;
import com.eneifour.fantry.bid.repository.BidRepository;
import com.eneifour.fantry.catalog.domain.GroupType;
import com.eneifour.fantry.inspection.domain.InventoryStatus;
import com.eneifour.fantry.inspection.domain.ProductInspection;
import com.eneifour.fantry.inspection.repository.InspectionRepository;
import com.eneifour.fantry.inspection.service.InspectionService;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.repository.MemberRepository;
import com.eneifour.fantry.orders.domain.OrderStatus;
import com.eneifour.fantry.orders.domain.Orders;
import com.eneifour.fantry.orders.repository.OrdersRepository;
import com.eneifour.fantry.auction.domain.AuctionClosedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification; // Specification 임포트 추가
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final RedisService redisService;
    private final InspectionRepository inspectionRepository;
    private final MemberRepository memberRepository;
    private final OrdersRepository ordersRepository;
    private final BidRepository bidRepository;
    private final InspectionService inspectionService; // InspectionService 의존성 추가
    private final AuctionSpecification auctionSpecification; // AuctionSpecification 의존성 추가
    private final ApplicationEventPublisher eventPublisher;

    // =============================================
    // 1. 상품 조회 (Read)
    // =============================================

    /**
     * 경매 상품의 존재 여부를 확인합니다.
     * @param auctionId 확인할 경매 ID
     */
    @Transactional(readOnly = true)
    public void isExistAuction(int auctionId){
        // 상품 존재 여부만 확인하고, 없으면 예외를 던집니다.
        auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));
    }

    /**
     * 조건에 따라 경매 상품 목록을 페이징하여 조회합니다.
     * AuctionSearchCondition DTO를 사용하여 동적으로 쿼리 조건을 생성합니다.
     * @param condition 검색 조건을 담는 DTO (saleType, saleStatus, groupType)
     * @param pageable 페이징 정보
     * @return 페이징된 경매 요약 정보 목록
     */
    @Transactional(readOnly = true)
    public Page<AuctionSummaryResponse> searchAuctions(AuctionSearchCondition condition, Pageable pageable) {
        // AuctionSearchCondition을 기반으로 Specification을 생성합니다.
        Specification<Auction> spec = auctionSpecification.toSpecification(condition);

        // 생성된 Specification과 Pageable을 사용하여 경매 목록을 조회합니다.
        Page<Auction> auctions = auctionRepository.findAll(spec, pageable);

        List<Auction> auctionList = auctions.getContent();
        if (auctionList.isEmpty()) {
            return Page.empty(pageable);
        }

        // 2. 조회된 경매 목록의 ID를 추출합니다.
        List<Integer> auctionIds = auctionList.stream()
                .map(Auction::getAuctionId)
                .collect(Collectors.toList());

        // 3. 경매 ID 목록으로 카테고리 이름 맵을 한 번에 조회합니다 (N+1 방지).
        //    (참고: 이 부분은 Auction 엔티티에 직접 categoryName 필드를 추가하거나,
        //     Specification 내에서 fetch join을 통해 최적화할 수 있습니다.)
        Map<Integer, String> categoryNameMap = auctionRepository.findCategoryNamesByAuctionIds(auctionIds).stream()
                .collect(Collectors.toMap(
                        row -> (Integer) row[0], // auctionId
                        row -> (String) row[1]  // categoryName
                ));

        // 4. 각 Auction 엔티티를 AuctionSummaryResponse DTO로 변환합니다.
        List<AuctionSummaryResponse> summaryResponses = auctionList.stream().map(auction -> {
            AuctionSummaryResponse summary = AuctionSummaryResponse.from(auction);
            Optional<GroupType> grouptype = inspectionRepository.findGroupTypeById(auction.getProductInspection().getProductInspectionId());
            // Redis에서 현재가를 조회하여 DTO에 설정
            summary.setCurrentPrice(redisService.getCurrentPrice(auction.getStartPrice(), auction.getAuctionId()));
            // Redis 에서 최고 입찰자를 조회하여 DTO에 설정
            summary.setHighestBidderId(redisService.getHighestBidderId(auction.getAuctionId()));
            // File info를 조회하여 DTO에 설정
            summary.setFileInfos(inspectionRepository.findFilesByProductInspectionId(auction.getProductInspection().getProductInspectionId()));
            // group type 조회하여 DTO 설정
            summary.setArtistGroupType( grouptype.map(Enum::name).orElse(null));
            // 조회해둔 카테고리 이름 맵에서 카테고리명을 찾아 설정
            summary.setCategoryName(categoryNameMap.get(auction.getAuctionId()));
            return summary;
        }).collect(Collectors.toList());

        // 5. 변환된 DTO 리스트와 페이징 정보를 사용하여 새로운 Page 객체를 생성하여 반환합니다.
        return new PageImpl<>(summaryResponses, pageable, auctions.getTotalElements());
    }

    /**
     * 특정 회원의 판매 상품을 판매 상태별로 조회합니다.
     * @param memberId 회원 ID
     * @param saleStatus 판매 상태
     * @return 경매 요약 정보 목록
     */
    @Transactional(readOnly = true)
    public List<AuctionSummaryResponse> findBymemberIdAndSaleStatus(int memberId, SaleStatus saleStatus){
        // 1. DB에서 조건에 맞는 경매 목록(List<Auction>)을 조회합니다.
        List<Auction> auctionList = auctionRepository.findByProductInspection_MemberIdAndSaleStatus(memberId, saleStatus);
        if (auctionList.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 조회된 경매 목록의 ID를 추출합니다.
        List<Integer> auctionIds = auctionList.stream()
                .map(Auction::getAuctionId)
                .collect(Collectors.toList());

        // 3. 경매 ID 목록으로 카테고리 이름 맵을 한 번에 조회합니다 (N+1 방지).
        Map<Integer, String> categoryNameMap = auctionRepository.findCategoryNamesByAuctionIds(auctionIds).stream()
                .collect(Collectors.toMap(
                        row -> (Integer) row[0],
                        row -> (String) row[1]
                ));

        // 4. 각 Auction 엔티티를 AuctionSummaryResponse DTO로 변환합니다.
        return auctionList.stream()
                .map(auction -> {
                    // 4-1. 기본 DTO로 변환
                    AuctionSummaryResponse summary = AuctionSummaryResponse.from(auction);
                    Optional<GroupType> grouptype = inspectionRepository.findGroupTypeById(auction.getProductInspection().getProductInspectionId());
                    // 4-2. Redis에서 현재가 조회 후 DTO 설정
                    summary.setCurrentPrice(redisService.getCurrentPrice(auction.getStartPrice(), auction.getAuctionId()));
                    // 4-3. File info 조회 후 DTO 설정
                    summary.setFileInfos(inspectionRepository.findFilesByProductInspectionId(auction.getProductInspection().getProductInspectionId()));
                    // 4-4. 조회해둔 카테고리 이름 맵에서 카테고리명을 찾아 설정
                    summary.setCategoryName(categoryNameMap.get(auction.getAuctionId()));
                    // 4-5. 그룹타입 설정
                    summary.setArtistGroupType( grouptype.map(Enum::name).orElse(null));
                    // 4-6. 완성된 DTO 반환
                    return summary;
                })
                .collect(Collectors.toList());
    }

    /**
     * 특정 경매 상품에 대해 주어진 회원이 낙찰자인지 확인하고, 낙찰자일 경우 주문 상태를 반환합니다.
     * @param auctionId 확인할 경매 ID
     * @param memberId 확인할 회원 ID
     * @return 낙찰자이고 주문이 존재하면 Optional<주문상태>, 아니면 Optional.empty()
     */
    @Transactional(readOnly = true)
    public Optional<String> getAuctionWinnerStatus(int auctionId, int memberId) {
        // auctionId로 주문(낙찰 정보)을 조회합니다.
        Optional<Orders> orderOpt = ordersRepository.findByAuction_AuctionId(auctionId);
        // 주문이 존재하지 않으면, 아직 낙찰자가 없는 상태이므로 빈 Optional을 반환합니다.
        if (orderOpt.isEmpty()) {
            return Optional.empty();
        }
        Orders order = orderOpt.get();
        // 주문의 구매자 ID와 요청한 회원의 ID가 일치하는지 확인합니다.
        if (order.getMember().getMemberId() == memberId) {
            // 일치하면, 해당 주문의 상태(Enum)를 문자열로 변환하여 Optional에 담아 반환합니다.
            return Optional.of(order.getOrderStatus().name());
        } else {
            // 주문은 있지만 요청한 회원이 구매자가 아닌 경우, 빈 Optional을 반환합니다.
            return Optional.empty();
        }
    }

    /**
     * 특정 회원이 등록한 모든 판매 상품을 조회합니다.
     * @param memberId 회원 ID
     * @return 경매 요약 정보 목록
     */
    @Transactional(readOnly = true)
    public List<AuctionSummaryResponse> findBymemberId(int memberId){
        // 1. DB에서 조건에 맞는 경매 목록(List<Auction>)을 조회합니다.
        List<Auction> auctionList = auctionRepository.findByProductInspection_MemberId(memberId);
        if (auctionList.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 조회된 경매 목록의 ID를 추출합니다.
        List<Integer> auctionIds = auctionList.stream()
                .map(Auction::getAuctionId)
                .collect(Collectors.toList());

        // 3. 경매 ID 목록으로 카테고리 이름 맵을 한 번에 조회합니다 (N+1 방지).
        Map<Integer, String> categoryNameMap = auctionRepository.findCategoryNamesByAuctionIds(auctionIds).stream()
                .collect(Collectors.toMap(
                        row -> (Integer) row[0],
                        row -> (String) row[1]
                ));

        // 4. 각 Auction 엔티티를 AuctionSummaryResponse DTO로 변환합니다.
        return auctionList.stream()
                .map(auction -> {
                    // 4-1. 기본 DTO로 변환
                    AuctionSummaryResponse summary = AuctionSummaryResponse.from(auction);
                    Optional<GroupType> grouptype = inspectionRepository.findGroupTypeById(auction.getProductInspection().getProductInspectionId());
                    // 4-2. Redis에서 현재가 조회 후 DTO 설정
                    summary.setCurrentPrice(redisService.getCurrentPrice(auction.getStartPrice(), auction.getAuctionId()));
                    // 4-3. File info 조회 후 DTO 설정
                    summary.setFileInfos(inspectionRepository.findFilesByProductInspectionId(auction.getProductInspection().getProductInspectionId()));
                    // 4-4. 조회해둔 카테고리 이름 맵에서 카테고리명을 찾아 설정
                    summary.setCategoryName(categoryNameMap.get(auction.getAuctionId()));
                    // 4-5. 그룹타입 설정
                    summary.setArtistGroupType( grouptype.map(Enum::name).orElse(null));
                    // 4-5. 완성된 DTO 반환
                    return summary;
                })
                .collect(Collectors.toList());
    }

    /**
     * 특정 회원이 입찰한 경매 중 활성 상태인 경매 ID 목록을 조회합니다.
     * @param memberId 회원 ID
     * @return 활성 경매 ID 목록
     */
    @Transactional(readOnly = true)
    public List<Integer> getActiveAuctionsBidByMember(int memberId) {
        List<Integer> auctionIds = auctionRepository.findActiveAuctionsBidByMember(memberId);
        return auctionIds != null ? auctionIds : Collections.emptyList();
    }

    /**
     * 경매 ID를 기준으로 경매 상세 정보를 조회합니다.
     * @param auctionId 조회할 경매 ID
     * @return 경매 상세 정보 DTO
     */
    @Transactional(readOnly = true)
    public AuctionDetailResponse findByAuctionId(int auctionId){
        // --- 1단계: 기본 정보 조회 (DB) ---
        // Repository에서 JPQL로 필요한 정보를 대부분 조인하여 DTO 형태로 가져옵니다.
        AuctionDetailResponse baseDetail = auctionRepository.findByAuctionId(auctionId)
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));

        // --- 2단계: 현재 최고 입찰가 및 입찰자 ID 결정 (Redis 우선 조회) ---
        int currentPrice = redisService.getCurrentPrice(baseDetail.getStartPrice(), auctionId);
        int highestBidderId = redisService.getHighestBidderId(auctionId);

        // --- 3단계: 파일 정보 목록 조회 ---
        List<AuctionDetailResponse.FileInfo> fileInfos = inspectionRepository.findFilesByProductInspectionId(baseDetail.getProductInspectionId());

        // --- 4단계: Builder로 모든 정보를 취합하여 최종 DTO 반환 ---
        return AuctionDetailResponse.builder()
                // --- 기본 정보 복사 ---
                .auctionId(baseDetail.getAuctionId())
                .productInspectionId(baseDetail.getProductInspectionId())
                .memberId(baseDetail.getMemberId())
                .saleStatus(baseDetail.getSaleStatus())
                .saleType(baseDetail.getSaleType())
                .startTime(baseDetail.getStartTime())
                .endTime(baseDetail.getEndTime())
                .itemName(baseDetail.getItemName())
                .itemDescription(baseDetail.getItemDescription())
                .hashtags(baseDetail.getHashtags())
                .categoryName(baseDetail.getCategoryName())
                .artistName(baseDetail.getArtistName())
                .artistGroupType(baseDetail.getArtistGroupType())
                .albumTitle(baseDetail.getAlbumTitle())
                .startPrice(baseDetail.getStartPrice())
                // --- 추가 정보 설정 ---
                .currentPrice(currentPrice)
                .highestBidderId(highestBidderId)
                .fileInfos(fileInfos)
                .build();
    }

    /**
     * 상품 검수 ID를 기준으로 가장 최근에 등록된 경매 1건의 상세 정보를 조회합니다.
     * @param productInspectionId 조회할 상품 검수 ID
     * @return 최신 경매 상세 정보 DTO
     */
    @Transactional(readOnly = true)
    public AuctionDetailResponse findByProductInspectionId(int productInspectionId) {
        // --- 1단계: 기본 정보 조회 (DB) ---
        // Repository에서 생성일 내림차순으로 정렬된 목록을 가져옵니다.
        List<AuctionDetailResponse> details = auctionRepository.findAuctionDetailsByProductInspectionIdOrderByCreatedAtDesc(productInspectionId);
        if (details.isEmpty()) {
            // 해당 검수 ID로 생성된 경매가 없으면 예외를 던집니다.
            throw new AuctionException(ErrorCode.AUCTION_NOT_FOUND);
        }
        // 가장 최근 항목(첫 번째 항목)을 기본 정보로 사용합니다.
        AuctionDetailResponse baseDetail = details.get(0);

        // --- 2단계: 현재 최고 입찰가 및 입찰자 ID 결정 (Redis 우선 조회) ---
        int auctionId = baseDetail.getAuctionId();
        int currentPrice = redisService.getCurrentPrice(baseDetail.getStartPrice(), auctionId);
        int highestBidderId = redisService.getHighestBidderId(auctionId);

        // --- 3단계: 파일 정보 목록 조회 ---
        List<AuctionDetailResponse.FileInfo> fileInfos = inspectionRepository.findFilesByProductInspectionId(baseDetail.getProductInspectionId());

        // --- 4단계: Builder로 모든 정보를 취합하여 최종 DTO 반환 ---
        return AuctionDetailResponse.builder()
                // --- 기본 정보 복사 ---
                .auctionId(baseDetail.getAuctionId())
                .productInspectionId(baseDetail.getProductInspectionId())
                .memberId(baseDetail.getMemberId())
                .saleStatus(baseDetail.getSaleStatus())
                .saleType(baseDetail.getSaleType())
                .startTime(baseDetail.getStartTime())
                .endTime(baseDetail.getEndTime())
                .itemName(baseDetail.getItemName())
                .itemDescription(baseDetail.getItemDescription())
                .hashtags(baseDetail.getHashtags())
                .categoryName(baseDetail.getCategoryName())
                .artistName(baseDetail.getArtistName())
                .artistGroupType(baseDetail.getArtistGroupType())
                .albumTitle(baseDetail.getAlbumTitle())
                .startPrice(baseDetail.getStartPrice())
                // --- 추가 정보 설정 ---
                .currentPrice(currentPrice)
                .highestBidderId(highestBidderId)
                .fileInfos(fileInfos)
                .build();
    }

    // =============================================
    // 2. 상품 등록/삭제 (Write)
    // =============================================

    /**
     * 새로운 경매 상품을 등록합니다.
     * @param request 경매 생성 요청 데이터
     * @return 생성된 경매 상세 정보 DTO
     */
    @Transactional
    public AuctionDetailResponse createAuction(AuctionRequest request) {
        // 1. 요청에 포함된 검수 ID로 검수 정보를 조회합니다. 없으면 예외 발생.
        ProductInspection productInspection = inspectionRepository.findById(request.getProductInspectionId())
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_MATCH_INSPECTION));


        SaleStatus saleStatus = Boolean.TRUE.equals(request.getIsReRegistration())
                ? SaleStatus.REPREPARING
                : SaleStatus.PREPARING;

        // 2. DTO와 조회된 엔티티를 바탕으로 Auction 엔티티를 생성합니다.
        Auction auction = Auction.builder()
                .productInspection(productInspection)
                .saleType(request.getSaleType())
                .startPrice(request.getStartPrice())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .saleStatus(saleStatus)
                .build();

        // 3. DB에 저장합니다.
        Auction savedAuction = auctionRepository.save(auction);

        // [상태 연동] 재고 상태를 PENDING_ACTIVE(판매 대기)로 변경합니다.
        inspectionService.updateInventoryStatus(productInspection.getProductInspectionId(), InventoryStatus.PENDING_ACTIVE);

        // 4. 저장된 엔티티의 ID를 사용하여 상세 DTO를 조회하여 반환합니다.
        return findByAuctionId(savedAuction.getAuctionId());
    }

    /**
     * 경매 상품을 삭제합니다.
     * @param auctionId 삭제할 경매 ID
     */
    @Transactional
    public void deleteAuction(int auctionId) {
        // 1. 유효한 상품인지 조회합니다.
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));

        // 진행중인 경매는 삭제할 수 없습니다.
        if (auction.getSaleStatus() == SaleStatus.ACTIVE) {
            throw new AuctionException(ErrorCode.CANNOT_DELETE_ACTIVE_AUCTION);
        }

        // 2. 통과하면 삭제합니다.
        auctionRepository.deleteById(auctionId);
    }

    /**
     * 단일 경매에 대한 마감 처리를 수행합니다. (주로 스케줄러에 의해 호출됨)
     * 이 메서드는 하나의 원자적 단위로 동작해야 하므로 @Transactional을 적용합니다.
     * @param auction 마감 처리할 경매 객체
     */
    @Transactional
    public void processAuctionClosure(Auction auction) {
        log.info("Processing closure for auctionId: {}", auction.getAuctionId());

        // 1. 해당 경매의 최고 입찰(낙찰자)을 DB에서 조회합니다.
        Optional<Bid> winningBidOptional = bidRepository.findTopByItemIdOrderByBidAmountDesc(auction.getAuctionId());

        if (winningBidOptional.isPresent()) {
            // --- Case 1: 낙찰자가 있는 경우 ---
            Bid winningBid = winningBidOptional.get();
            int finalPrice = winningBid.getBidAmount();
            int winnerId = winningBid.getBidderId();

            // 1-1. 낙찰자 정보를 조회합니다.
            Member winner = memberRepository.findById(winnerId)
                    .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

            // 1-2. 경매 상태를 '판매 완료(SOLD)'로 변경하고 최종 낙찰가를 기록합니다.
            auction.closeAsSold(finalPrice);

            // 1-3. 새로운 주문(Orders)을 생성합니다.
            Orders newOrder = Orders.builder()
                    .auction(auction)
                    .member(winner)
                    .orderStatus(OrderStatus.PENDING_PAYMENT) // 초기 주문 상태는 '결제 대기중'
                    .price(finalPrice)
                    .build();

            ordersRepository.save(newOrder);

            // [상태 연동] 재고 상태를 SOLD(판매 완료)로 변경합니다.
            inspectionService.updateInventoryStatus(auction.getProductInspection().getProductInspectionId(), InventoryStatus.SOLD);

            log.info("AuctionId {} has been successfully closed. Winner: {}, Final Price: {}",
                    auction.getAuctionId(), winner.getName(), finalPrice);

            // TODO: 낙찰자에게 알림을 보내는 로직을 추가할 수 있음 (e.g., WebSocket, SMS, Email)
            // 트랜잭션 커밋 후 이벤트 발행 (알림 전송)
            eventPublisher.publishEvent(new AuctionClosedEvent(auction.getAuctionId()));

        } else {
            // --- Case 2: 입찰자가 아무도 없어 유찰된 경우 ---
            log.info("AuctionId {} has ended with no bids. Status changed to NOT_SOLD.", auction.getAuctionId());
            // 경매 상태를 '판매 실패(NOT_SOLD)'로 변경합니다.
            auction.closeAsNotSold();

            // [상태 연동] 재고 상태를 NOT_SOLD(판매 안됨)로 변경합니다.
            inspectionService.updateInventoryStatus(auction.getProductInspection().getProductInspectionId(), InventoryStatus.NOT_SOLD);

            // TODO: 판매자에게 유찰 알림을 보내는 로직을 추가할 수 있습니다.
        }
        // 변경된 경매 상태를 DB에 최종 저장합니다.
        auctionRepository.save(auction);
    }

    // =============================================
    // 3. 상품 수정 (Update)
    // =============================================

    /**
     * 상품을 판매 완료 상태로 변경합니다.
     * @param auctionId 처리할 상품 ID
     * @param finalPrice 최종 판매 가격
     */
    @Transactional
    public void markAsSold(int auctionId, int finalPrice) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));
        auction.closeAsSold(finalPrice);
    }

    /**
     * 상품을 판매 실패(유찰) 상태로 변경합니다. (주로 스케줄러에서 사용)
     * @param auctionId 처리할 상품 ID
     */
    @Transactional
    public void markAsNotSold(int auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));
        auction.closeAsNotSold();
    }

    /**
     * 상품을 판매 취소 상태로 변경합니다.
     * @param auctionId 처리할 상품 ID
     */
    @Transactional
    public void cancelAuction(int auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));

        // 무한 루프 방지를 위한 가드 코드: 이미 취소된 상태이면 더 이상 진행하지 않습니다.
        if (auction.getSaleStatus() == SaleStatus.CANCELED) {
            return;
        }

        auction.closeAsCancelled();

        // [상태 연동] 재고 상태를 CANCELLED(판매 취소)로 변경합니다.
        inspectionService.updateInventoryStatus(auction.getProductInspection().getProductInspectionId(), InventoryStatus.CANCELED);
    }

    /**
     * 상품을 다시 판매 중(활성) 상태로 변경합니다.
     * @param auctionId 처리할 상품 ID
     */
    @Transactional
    public void activateAuction(int auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));

        if(auction.getSaleStatus() == SaleStatus.PREPARING){
            auction.activate();
        }else if(auction.getSaleStatus() == SaleStatus.REPREPARING){
            auction.reactivate();
        }

        // [상태 연동] 재고 상태를 ACTIVE(판매중)로 변경합니다.
        inspectionService.updateInventoryStatus(auction.getProductInspection().getProductInspectionId(), InventoryStatus.ACTIVE);
    }

    /**
     * 상품의 판매 유형을 변경합니다.
     * @param auctionId 처리할 상품 ID
     * @param newSaleType 새로운 판매 유형
     */
    @Transactional
    public void changeSaleType(int auctionId, String newSaleType) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));

        // 활성 상태의 경매는 판매 유형을 변경할 수 없습니다.
        if (auction.getSaleStatus() == SaleStatus.ACTIVE) {
            throw new AuctionException(ErrorCode.CANNOT_RESCHEDULE_ACTIVE_AUCTION);
        }

        auction.changeSaleType(newSaleType);
    }
}
