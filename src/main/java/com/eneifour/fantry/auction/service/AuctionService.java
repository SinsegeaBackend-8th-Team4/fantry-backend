package com.eneifour.fantry.auction.service;

import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.auction.domain.SaleStatus;
import com.eneifour.fantry.auction.domain.SaleType;
import com.eneifour.fantry.auction.dto.AuctionDetailResponse;
import com.eneifour.fantry.auction.dto.AuctionRequest;
import com.eneifour.fantry.auction.dto.AuctionSummaryResponse;
import com.eneifour.fantry.auction.exception.AuctionException;
import com.eneifour.fantry.auction.exception.ErrorCode;
import com.eneifour.fantry.auction.repository.AuctionRepository;
import com.eneifour.fantry.inspection.domain.ProductInspection;
import com.eneifour.fantry.inspection.dto.OnlineInspectionDetailResponse;
import com.eneifour.fantry.inspection.repository.InspectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final RedisService redisService;
    private final InspectionRepository inspectionRepository;

    // =============================================
    // 1. 상품 조회 (Read)
    // =============================================

    // 상품 전체 내역 조회 (pageable 사용)
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
            summary.setCurrentPrice(currentPrice);
            return summary;
        });
    }

    // 판매 상품 중 , member_id 를 기준으로 sale_status가 특정한 것 조회
    public List<Auction> findBymemberIdAndSaleStatus(int memberId, SaleStatus saleStatus){
        return auctionRepository.findByProductInspection_MemberIdAndSaleStatus(memberId, saleStatus);
    }

    // 판매 상품 중 , member_id 를 기준으로 모든 상품 조회
    public List<Auction> findBymemberId(int memberId){
        return auctionRepository.findByProductInspection_MemberId(memberId);
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

        // ---3단계 : Builder 로 DTO 반환
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
