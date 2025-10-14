package com.eneifour.fantry.auction.controller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 상품(경매) 관련 API를 제공하는 컨트롤러입니다.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auctions")
public class AuctionController {
    private final AuctionService auctionService;

    /**
     * 특정 상품의 상세 정보를 조회합니다.
     *
     * @param auctionId 조회할 상품의 ID
     * @return 상품 상세 정보.
     */
    @GetMapping("/{auctionId}")
    public ResponseEntity<?> getAuctionById(@PathVariable("auctionId") int auctionId){
        AuctionDetailResponse auctionDetail = auctionService.findOne(auctionId);

        return ResponseEntity.ok(auctionDetail);
    }


    /**
     * 특정 상품에 대한 회원의 낙찰 여부 및 결제 상태를 조회합니다.
     * <p>해당 상품의 낙찰자가 맞으면 주문의 현재 상태(예: "PAID", "SHIPPED")를 반환합니다.
     * <p>낙찰자가 아니거나, 아직 낙찰자가 정해지지 않은 경우 "USER"를 반환합니다.
     *
     * @param auctionId 조회할 상품의 ID
     * @param memberId  조회할 회원의 ID
     * @return 주문 상태 문자열 또는 "USER".
     */
    @GetMapping("/{auctionId}/winner-status")
    public ResponseEntity<String> getAuctionWinnerStatus(
            @PathVariable int auctionId,
            @RequestParam int memberId) {
        log.info("Request to check winner status for auctionId: {} and memberId: {}", auctionId, memberId);

        Optional<String> statusOptional = auctionService.getAuctionWinnerStatus(auctionId, memberId);

        // Service에서 반환된 Optional 객체가 값을 가지고 있으면(isPresent), 그 값을 body로 사용합니다.
        // 값이 없으면(empty), orElse("USER")를 통해 "USER"를 기본값으로 사용합니다.
        String responseBody = statusOptional.orElse("USER");

        return ResponseEntity.ok(responseBody);
    }

    /**
     * 상품 목록을 조건에 따라 페이징하여 조회합니다.
     * <p>판매 유형(saleType)이나 판매 상태(saleStatus)를 지정하여 필터링할 수 있습니다.
     * <p>아무 조건도 지정하지 않으면 전체 상품 목록이 반환됩니다.
     *
     * @param saleType   판매 유형 (AUCTION, INSTANT_BUY).
     * @param saleStatus 판매 상태 (ACTIVE, SOLD, NOT_SOLD, CANCELLED).
     * @param pageable   페이징 정보 (페이지 번호, 페이지 크기 등).
     * @return 페이징 처리된 상품 요약 목록.
     */
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
     * 특정 회원이 등록한 모든 판매 상품 목록을 조회합니다.
     *
     * @param memberId 조회할 회원의 ID
     * @return 해당 회원의 상품 요약 목록.
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<AuctionSummaryResponse>> getAuctionsByMember(@PathVariable int memberId) {
        log.info("Request to get auctions for memberId: {}", memberId);
        List<AuctionSummaryResponse> auctions = auctionService.findBymemberId(memberId);
        return ResponseEntity.ok(auctions);
    }

    /**
     * 특정 회원의 판매 상태별 상품 목록을 조회합니다.
     *
     * @param memberId   조회할 회원의 ID
     * @param saleStatus 조회할 판매 상태  (ACTIVE, SOLD, NOT_SOLD, CANCELLED).
     * @return 해당 조건에 맞는 상품 요약 목록.
     */
    @GetMapping("/member/{memberId}/status")
    public ResponseEntity<List<AuctionSummaryResponse>> getAuctionsByMemberAndStatus(
            @PathVariable int memberId,
            @RequestParam SaleStatus saleStatus) {
        log.info("Request to get auctions for memberId: {} with status: {}", memberId, saleStatus);
        List<AuctionSummaryResponse> auctions = auctionService.findBymemberIdAndSaleStatus(memberId, saleStatus);
        return ResponseEntity.ok(auctions);
    }

    /**
     * 새로운 판매 상품을 등록합니다.
     * <p>요청 본문(Request Body)에 상품 정보를 담아 전송해야 합니다.
     *
     * @param request 상품 등록에 필요한 데이터.
     *                <p><b>[요청 필드]</b></p>
     *                <ul>
     *                  <li><b>product_inspection_id</b>: 상품 검수 ID (필수)</li>
     *                  <li><b>sale_type</b>: 판매 유형 (AUCTION, INSTANT_BUY) (필수)</li>
     *                  <li><b>start_price</b>: 시작 가격 (필수, 100원 이상)</li>
     *                  <li><b>start_time</b>: 판매 시작 시간 (필수)</li>
     *                  <li><b>end_time</b>: 판매 종료 시간 (필수, 현재보다 미래)</li>
     *                </ul>
     * @return 작업 성공 메시지.
     */
    @PostMapping
    public ResponseEntity<?> createAuction(@Valid @RequestBody AuctionRequest request) {
        log.info("Request to create auction for inspectionId: {}", request.getProductInspectionId());
        auctionService.createAuction(request);
        return ResponseEntity.ok("상품 등록 완료");
    }

    /**
     * 등록된 판매 상품을 삭제합니다.
     *
     * @param auctionId 삭제할 상품의 ID
     * @return 작업 성공 메시지.
     */
    @DeleteMapping("/{auctionId}")
    public ResponseEntity<String> deleteAuction(@PathVariable int auctionId) {
        log.info("Request to delete auction: {}", auctionId);
        auctionService.deleteAuction(auctionId);
        return ResponseEntity.ok("Auction ID " + auctionId + " has been successfully deleted.");
    }

    /**
     * 상품을 판매 완료 상태로 변경합니다.
     *
     * @param auctionId  처리할 상품의 ID
     * @param finalPrice 최종 판매 가격
     * @return 작업 성공 메시지.
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
     * 상품을 판매 실패(유찰) 상태로 변경합니다.
     *
     * @param auctionId 처리할 상품의 ID
     * @return 작업 성공 메시지.
     */
    @PatchMapping("/{auctionId}/status/not-sold")
    public ResponseEntity<String> markAsNotSold(@PathVariable int auctionId) {
        log.info("Request to mark auction {} as NOT_SOLD", auctionId);
        auctionService.markAsNotSold(auctionId);
        return ResponseEntity.ok("Auction ID " + auctionId + " marked as NOT_SOLD.");
    }

    /**
     * 상품을 판매 취소 상태로 변경합니다.
     *
     * @param auctionId 처리할 상품의 ID
     * @return 작업 성공 메시지.
     */
    @PatchMapping("/{auctionId}/status/cancelled")
    public ResponseEntity<String> cancelAuction(@PathVariable int auctionId) {
        log.info("Request to cancel auction {}", auctionId);
        auctionService.cancelAuction(auctionId);
        return ResponseEntity.ok("Auction ID " + auctionId + " marked as CANCELLED.");
    }

    /**
     * 상품을 다시 판매 중(활성) 상태로 변경합니다.
     *
     * @param auctionId 처리할 상품의 ID
     * @return 작업 성공 메시지.
     */
    @PatchMapping("/{auctionId}/status/active")
    public ResponseEntity<String> activateAuction(@PathVariable int auctionId) {
        log.info("Request to activate auction {}", auctionId);
        auctionService.activateAuction(auctionId);
        return ResponseEntity.ok("Auction ID " + auctionId + " activated.");
    }

    /**
     * 상품의 판매 유형을 변경합니다. (예: 경매 -> 즉시 구매)
     *
     * @param auctionId   처리할 상품의 ID
     * @param newSaleType 새로운 판매 유형 (AUCTION, INSTANT_BUY).
     * @return 작업 성공 메시지.
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
