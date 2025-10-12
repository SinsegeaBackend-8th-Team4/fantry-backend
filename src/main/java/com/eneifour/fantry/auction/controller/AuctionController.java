package com.eneifour.fantry.auction.controller;

import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.auction.domain.SaleStatus;
import com.eneifour.fantry.auction.domain.SaleType;
import com.eneifour.fantry.auction.dto.AuctionDetailResponse;
import com.eneifour.fantry.auction.dto.AuctionRequest;
import com.eneifour.fantry.auction.dto.AuctionSummaryResponse;
import com.eneifour.fantry.auction.service.AuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auctions")
public class AuctionController {
    private final AuctionService auctionService;

    // =============================================
    // 1. 상품 조회 (Read)
    // =============================================

    /**
     * 상품 단건 조회
     */
    @GetMapping("/{auctionId}")
    public ResponseEntity<?> getAuctionById(@PathVariable("auctionId") int auctionId){
        log.warn("/api/auctions/"+auctionId);
        AuctionDetailResponse auctionDetail = auctionService.findOne(auctionId);

        return ResponseEntity.ok(auctionDetail);
    }

    /*
    ** 상품 List 조회 (Status 및 saleType 을 명시할 경우 , 조건에 맞는 상품 출력 -> 서비스 분기 처리)
    *  status 및 saleType 이 null 일 경우 전체 목록 반환
    *  Page 는 기본적으로 명시하지 않으면 default 10건씩 반환
    * */
    @GetMapping
    public ResponseEntity<Page<AuctionSummaryResponse>> getAuctions(
            @RequestParam(required = false) SaleType saleType,
            @RequestParam(required = false) SaleStatus saleStatus,
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Request to search auctions with saleType: {} and saleStatus: {}", saleType, saleStatus);
        Page<AuctionSummaryResponse> auctions = auctionService.searchAuctions(saleType, saleStatus, pageable);
        return ResponseEntity.ok(auctions);
    }

    /**
     * 특정 회원의 모든 판매 상품 조회
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Auction>> getAuctionsByMember(@PathVariable int memberId) {
        log.info("Request to get auctions for memberId: {}", memberId);
        List<Auction> auctions = auctionService.findBymemberId(memberId);
        return ResponseEntity.ok(auctions);
    }

    /**
     *  특정 회원의 판매 상태별 상품 조회
     */
    @GetMapping("/member/{memberId}/status")
    public ResponseEntity<List<Auction>> getAuctionsByMemberAndStatus(
            @PathVariable int memberId,
            @RequestParam SaleStatus saleStatus) {
        log.info("Request to get auctions for memberId: {} with status: {}", memberId, saleStatus);
        List<Auction> auctions = auctionService.findBymemberIdAndSaleStatus(memberId, saleStatus);
        return ResponseEntity.ok(auctions);
    }

    // =============================================
    // 2. 상품 등록/삭제 (Write)
    // =============================================

    /**
     * 새로운 판매 상품 등록
     */
    @PostMapping
    public ResponseEntity<?> createAuction(@Valid @RequestBody AuctionRequest request) {
        log.info("Request to create auction for inspectionId: {}", request.getProductInspectionId());
        AuctionDetailResponse createdAuction = auctionService.createAuction(request);
        return ResponseEntity.ok("상품 등록 완료");
    }

    /**
     * 판매 상품 삭제
     */
    @DeleteMapping("/{auctionId}")
    public ResponseEntity<String> deleteAuction(@PathVariable int auctionId) {
        log.info("Request to delete auction: {}", auctionId);
        auctionService.deleteAuction(auctionId);
        return ResponseEntity.ok("Auction ID " + auctionId + " has been successfully deleted.");
    }


    // =============================================
    // 2. 상품 수정 (Update)
    // =============================================
    /**
     * 상품 판매 완료 처리
     */
    @PatchMapping("/{auctionId}/status/sold")
    public ResponseEntity<String> markAsSold(
            @PathVariable int auctionId,
            @RequestParam int finalPrice) {
        log.info("Request to mark auction {} as SOLD with finalPrice {}", auctionId, finalPrice);
        auctionService.markAsSold(auctionId, finalPrice);
        return ResponseEntity.ok("Auction ID " + auctionId + " marked as SOLD.");
    }

    /**
     * 상품을 판매 실패(NOT_SOLD) 상태로 변경
     */
    @PatchMapping("/{auctionId}/status/not-sold")
    public ResponseEntity<String> markAsNotSold(@PathVariable int auctionId) {
        log.info("Request to mark auction {} as NOT_SOLD", auctionId);
        auctionService.markAsNotSold(auctionId);
        return ResponseEntity.ok("Auction ID " + auctionId + " marked as NOT_SOLD.");
    }

    /**
     * 상품을 취소(CANCELLED) 상태로 변경
     */
    @PatchMapping("/{auctionId}/status/cancelled")
    public ResponseEntity<String> cancelAuction(@PathVariable int auctionId) {
        log.info("Request to cancel auction {}", auctionId);
        auctionService.cancelAuction(auctionId);
        return ResponseEntity.ok("Auction ID " + auctionId + " marked as CANCELLED.");
    }

    /**
     * 상품을 활성(ACTIVE) 상태로 변경
     */
    @PatchMapping("/{auctionId}/status/active")
    public ResponseEntity<String> activateAuction(@PathVariable int auctionId) {
        log.info("Request to activate auction {}", auctionId);
        auctionService.activateAuction(auctionId);
        return ResponseEntity.ok("Auction ID " + auctionId + " activated.");
    }

    /**
     * 상품 판매 유형 변경
     */
    @PatchMapping("/{auctionId}/sale-type")
    public ResponseEntity<String> changeSaleType(
            @PathVariable int auctionId,
            @RequestParam String newSaleType) {
        log.info("Request to change saleType of auction {} to {}", auctionId, newSaleType);
        auctionService.changeSaleType(auctionId, newSaleType);
        return ResponseEntity.ok("Auction ID " + auctionId + " saleType changed to " + newSaleType + ".");
    }
}
