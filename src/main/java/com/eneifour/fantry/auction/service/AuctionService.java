package com.eneifour.fantry.auction.service;

import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.auction.domain.SaleStatus;
import com.eneifour.fantry.auction.domain.SaleType;
import com.eneifour.fantry.auction.dto.AuctionDetailResponse;
import com.eneifour.fantry.auction.dto.AuctionRequest;
import com.eneifour.fantry.auction.dto.AuctionSummaryResponse;
import com.eneifour.fantry.auction.exception.AuctionException;
import com.eneifour.fantry.auction.exception.ErrorCode;
import com.eneifour.fantry.auction.exception.MemberException;
import com.eneifour.fantry.auction.repository.AuctionRepository;
import com.eneifour.fantry.bid.domain.Bid;
import com.eneifour.fantry.bid.repository.BidRepository;
import com.eneifour.fantry.inspection.domain.ProductInspection;
import com.eneifour.fantry.inspection.dto.OnlineInspectionDetailResponse;
import com.eneifour.fantry.inspection.repository.InspectionRepository;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.repository.MemberRepository;
import com.eneifour.fantry.orders.domain.OrderStatus;
import com.eneifour.fantry.orders.domain.Orders;
import com.eneifour.fantry.orders.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
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

    // =============================================
    // 1. 상품 조회 (Read)
    // =============================================

    //상품 존재 여부 파악
    @Transactional
    public void isExistAuction(int auctionId){
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));
    }

    // 상품 전체 내역 조회 (pageable 사용)
    @Transactional
    public Page<AuctionSummaryResponse> searchAuctions(SaleType saleType , SaleStatus saleStatus, Pageable pageable) {

        Page<Auction> auctions;

        if (saleType != null && saleStatus != null) {
            auctions = auctionRepository.findBySaleTypeAndSaleStatus(saleType, saleStatus, pageable);
        }else if (saleType != null) {
            auctions = auctionRepository.findBySaleType(saleType, pageable);
        }else if (saleStatus != null) {
            auctions = auctionRepository.findBySaleStatus(saleStatus, pageable);
        }else {
            auctions = auctionRepository.findAll(pageable);
        }

        return auctions.map(auction -> {
            AuctionSummaryResponse summary = AuctionSummaryResponse.from(auction);
            // Redis에서 현재가를 조회하여 DTO에 설정
            int currentPrice = redisService.getCurrentPrice(auction.getAuctionId(), auction.getStartPrice());
            //File info를 조회하여 DTO에 설정
            int inspectionId = auction.getProductInspection().getProductInspectionId();
            List<AuctionDetailResponse.FileInfo> fileInfos = inspectionRepository.findFilesByProductInspectionId(inspectionId);

            summary.setCurrentPrice(currentPrice);
            summary.setFileInfos(fileInfos);
            return summary;
        });
    }

    // 판매 상품 중 , member_id 를 기준으로 sale_status가 특정한 것 조회
    @Transactional
    public List<AuctionSummaryResponse> findBymemberIdAndSaleStatus(int memberId, SaleStatus saleStatus){
        List<Auction> auctionList = auctionRepository.findByProductInspection_MemberIdAndSaleStatus(memberId, saleStatus);

        return auctionList.stream()
                .map(auction -> {
                    //1. 기본 DTO로 변환
                    AuctionSummaryResponse summary = AuctionSummaryResponse.from(auction);

                    //2. Redis에서 현재가 조회후 DTO 설정
                    int  currentPrice = redisService.getCurrentPrice(auction.getAuctionId(), auction.getStartPrice());
                    summary.setCurrentPrice(currentPrice);

                    //3. File info 조회 후 DTO 설정
                    int inspectionId = auction.getProductInspection().getProductInspectionId();
                    List<AuctionDetailResponse.FileInfo> fileInfos = inspectionRepository.findFilesByProductInspectionId(inspectionId);
                    summary.setFileInfos(fileInfos);

                    //4. 완성된 DTO 반환
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
        // Orders 엔티티에 getBuyer().getMemberId() 와 같은 getter가 있다고 가정합니다.
        if (order.getMember().getMemberId() == memberId) {
            // 일치하면, 해당 주문의 상태(Enum)를 문자열로 변환하여 Optional에 담아 반환합니다.
            return Optional.of(order.getOrderStatus().name());
        } else {
            // 주문은 있지만 요청한 회원이 구매자가 아닌 경우, 빈 Optional을 반환합니다.
            return Optional.empty();
        }
    }


    // 판매 상품 중 , member_id 를 기준으로 모든 상품 조회
    @Transactional
    public List<AuctionSummaryResponse> findBymemberId(int memberId){
        List<Auction> auctionList = auctionRepository.findByProductInspection_MemberId(memberId);

        return auctionList.stream()
                .map(auction -> {
                    //1. 기본 DTO로 변환
                    AuctionSummaryResponse summary = AuctionSummaryResponse.from(auction);

                    //2. Redis에서 현재가 조회후 DTO 설정
                    int  currentPrice = redisService.getCurrentPrice(auction.getAuctionId(), auction.getStartPrice());
                    summary.setCurrentPrice(currentPrice);

                    //3. File info 조회 후 DTO 설정
                    int inspectionId = auction.getProductInspection().getProductInspectionId();
                    List<AuctionDetailResponse.FileInfo> fileInfos = inspectionRepository.findFilesByProductInspectionId(inspectionId);
                    summary.setFileInfos(fileInfos);

                    //4. 완성된 DTO 반환
                    return summary;

                })
                .collect(Collectors.toList());
    }

    //Member_id 기준으로 입찰했던 Auction 의 Status가 Active 인 List 조회
    public List getActiveAuctionsBidByMember(int memberId) {
        List<Integer> auctionIds = auctionRepository.findActiveAuctionsBidByMember(memberId);
        return auctionIds != null ? auctionIds : Collections.emptyList();
    }

    //Auction_id 를 이용한 1건 조회
    @Transactional
    public AuctionDetailResponse findOne(int auctionId){
        // --- 1단계: 기본 정보 조회 (DB) ---
        AuctionDetailResponse baseDetail = auctionRepository.findAuctionDetailById(auctionId)
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));

        // --- 2단계: 현재 최고 입찰가 결정 (Redis 우선 조회 및 DB 교차 검증) ---
        int startPrice = baseDetail.getStartPrice();
        int currentPrice = redisService.getCurrentPrice(startPrice,auctionId);
        int highestBidderId = redisService.getHighestBidderId(auctionId);

        // ---3단계 : 파일 정보 목록 조회 ---
        List<AuctionDetailResponse.FileInfo> fileInfos = inspectionRepository.findFilesByProductInspectionId(baseDetail.getProductInspectionId());

        // ---4단계 : Builder 로 DTO 반환
        return AuctionDetailResponse.builder()
                // --- 기본 정보 복사 ---
                .auctionId(baseDetail.getAuctionId())
                .saleStatus(baseDetail.getSaleStatus())
                .saleType(baseDetail.getSaleType())
                .startTime(baseDetail.getStartTime())
                .endTime(baseDetail.getEndTime())
                .memberId(baseDetail.getMemberId())
                .itemName(baseDetail.getItemName())
                .itemDescription(baseDetail.getItemDescription())
                .categoryName(baseDetail.getCategoryName())
                .artistName(baseDetail.getArtistName())
                .albumTitle(baseDetail.getAlbumTitle())
                .startPrice(baseDetail.getStartPrice())
                .currentPrice(currentPrice)
                .highestBidderId(highestBidderId)
                .fileInfos(fileInfos)
                .build();
    }

    // =============================================
    // 2. 상품 등록/삭제 (Write)
    // =============================================

    //판매 상품 1건 등록 
    @Transactional
    public AuctionDetailResponse createAuction(AuctionRequest request) {
        ProductInspection productInspection = inspectionRepository.findById(request.getProductInspectionId())
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_MATCH_INSPECTION));

        // 2. DTO와 조회된 엔티티를 바탕으로 Auction 엔티티 생성
        Auction auction = Auction.builder()
                .productInspection(productInspection)
                .saleType(request.getSaleType())
                .startPrice(request.getStartPrice())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .saleStatus(SaleStatus.PREPARING)
                .build();

        // 3. DB에 저장
        Auction savedAuction = auctionRepository.save(auction);

        // 4. 저장된 엔티티를 바탕으로 상세 DTO를 생성하여 반환 (ID 등 생성된 정보 포함)
        return findOne(savedAuction.getAuctionId());
    }

    //판매 상품 삭제
    @Transactional
    public void deleteAuction(int auctionId) {
        // 1. 유효한 상품인지 조회 및 진행중인 경매 는 삭제 불가
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));

        if (auction.getSaleStatus() == SaleStatus.ACTIVE) {
            throw new AuctionException(ErrorCode.CANNOT_RESCHEDULE_ACTIVE_AUCTION);
        }

        // 2. 통과하면 삭제
        auctionRepository.deleteById(auctionId);
    }

    /**
     * 단일 경매에 대한 마감 처리를 수행합니다.
     * 이 메서드는 하나의 원자적 단위로 동작해야 하므로 @Transactional을 적용.
     * //@param auction 마감 처리할 경매 객체
     */
    @Transactional
    public void processAuctionClosure(Auction auction) {
        log.info("Processing closure for auctionId: {}", auction.getAuctionId());

        // 1. 해당 경매의 최고 입찰(낙찰자)을 DB에서 조회
        Optional<Bid> winningBidOptional = bidRepository.findTopByItemIdOrderByBidAmountDesc(auction.getAuctionId());

        if (winningBidOptional.isPresent()) {
            // --- Case 1: 낙찰자가 있는 경우 ---
            Bid winningBid = winningBidOptional.get();
            int finalPrice = winningBid.getBidAmount();
            int winnerId = winningBid.getBidderId();

            // 1-1. 낙찰자 정보를 조회
            Member winner = memberRepository.findById(winnerId)
                    .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

            // 1-2. 경매 상태를 '완료'로 변경하고 최종 낙찰가 기록
            auction.closeAsSold(finalPrice);

            // 1-3. 새로운 주문(Orders) 생성
            Orders newOrder = Orders.builder()
                    .auction(auction)
                    .member(winner)
                    .orderStatus(OrderStatus.PENDING_PAYMENT)
                    .price(finalPrice)
                    .build();

            // shippingAddress 등은 이후 사용자가 입력하도록 비워둠

            ordersRepository.save(newOrder);

            log.info("AuctionId {} has been successfully closed. Winner: {}, Final Price: {}",
                    auction.getAuctionId(), winner.getName(), finalPrice);

            // TODO: 낙찰자에게 알림을 보내는 로직을 추가할 수 있음 (e.g., WebSocket, SMS, Email)

        } else {
            // --- Case 2: 입찰자가 아무도 없어 유찰된 경우 ---
            log.info("AuctionId {} has ended with no bids. Status changed to CANCELLED.", auction.getAuctionId());
            auction.closeAsNotSold();
            // TODO: 판매자에게 유찰 알림을 보내는 로직을 추가할 수 있음
        }
        auctionRepository.save(auction);
    }

    // =============================================
    // 3. 상품 수정 (Update)
    // =============================================

    // 일반 판매 상품 판매 완료 처리
    // Param : 마지막 판매가
    @Transactional
    public void markAsSold(int auctionId, int finalPrice) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));

        auction.closeAsSold(finalPrice);
    }

    // 상품 상태를 'NOT_SOLD'로 변경 (기본적으로 스케줄러에서만 활용)
    @Transactional
    public void markAsNotSold(int auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));

        auction.closeAsNotSold();
    }

    // 상품 상태를 'CANCELLED'로 변경
    @Transactional
    public void cancelAuction(int auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));

        auction.closeAsCancelled();
    }

    // 상품 상태를 'ACTIVE'로 변경
    @Transactional
    public void activateAuction(int auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));

        auction.activate();
    }

    // 상품 판매유형 변경
    @Transactional
    public void changeSaleType(int auctionId, String newSaleType) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));

        if (auction.getSaleStatus() == SaleStatus.ACTIVE) {
            throw new AuctionException(ErrorCode.CANNOT_RESCHEDULE_ACTIVE_AUCTION);
        }

        auction.changeSaleType(newSaleType);
    }

}
