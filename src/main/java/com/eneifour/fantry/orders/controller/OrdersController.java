package com.eneifour.fantry.orders.controller;

import com.eneifour.fantry.orders.domain.OrderStatus;
import com.eneifour.fantry.orders.dto.OrdersRequest;
import com.eneifour.fantry.orders.dto.OrdersResponse;
import com.eneifour.fantry.orders.service.OrdersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrdersController {
    private final OrdersService ordersService;

    // =============================================
    // 1. 주문 조회 (Read)
    // =============================================

    /**
     * 주문 목록 조회
     * - memberId 또는 orderStatus 파라미터로 조건부 검색 가능
     * - 파라미터가 없으면 전체 주문 목록 반환
     * - Page는 명시하지 않으면 기본 10건씩 반환
     */
    @GetMapping
    public ResponseEntity<Page<OrdersResponse>> getOrders(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) Integer memberId,
            @RequestParam(required = false) OrderStatus orderStatus) {
        log.info("Request to search orders with memberId: {} and orderStatus: {}", memberId, orderStatus);
        Page<OrdersResponse> orders = ordersService.searchOrders(pageable, memberId, orderStatus);
        return ResponseEntity.ok(orders);
    }

    /**
     * 주문 단건 조회
     */
    @GetMapping("/{ordersId}")
    public ResponseEntity<OrdersResponse> getOrderById(@PathVariable("ordersId") int ordersId) {
        log.info("Request to get order detail for ordersId: {}", ordersId);
        OrdersResponse order = ordersService.findOne(ordersId);
        return ResponseEntity.ok(order);
    }

    // =============================================
    // 2. 주문 생성 (Write)
    // =============================================

    /**
     * 즉시 구매 상품에 대한 신규 주문 생성
     */
    @PostMapping("/instant-buy")
    public ResponseEntity<?> createInstantBuyOrder(@Valid @RequestBody OrdersRequest request) {
        log.info("Request to create instant-buy order for auctionId: {}", request.getAuctionId());
        OrdersResponse createdOrder = ordersService.createInstantBuyOrder(request);
        return ResponseEntity.ok(createdOrder);
    }

    // =============================================
    // 3. 주문 수정 (Update)
    // =============================================

    /**
     * 낙찰된 주문 건에 대한 결제 처리
     */
    @PatchMapping("/{ordersId}/payment")
    public ResponseEntity<String> completeAuctionPayment(
            @PathVariable int ordersId,
            @RequestParam String shippingAddress,
            @RequestParam int paymentId) {
        log.info("Request to complete payment for orderId: {}", ordersId);
        ordersService.completeAuctionPayment(shippingAddress, ordersId, paymentId);
        return ResponseEntity.ok("Order ID " + ordersId + " payment has been completed.");
    }

    /**
     * 주문 상태를 '배송 준비중'으로 변경
     */
    @PatchMapping("/{ordersId}/status/prepare-shipment")
    public ResponseEntity<String> prepareShipment(@PathVariable int ordersId) {
        log.info("Request to prepare shipment for orderId: {}", ordersId);
        ordersService.prepareShipment(ordersId);
        return ResponseEntity.ok("Order ID " + ordersId + " is preparing for shipment.");
    }

    /**
     * 주문 상태를 '배송중'으로 변경
     */
    @PatchMapping("/{ordersId}/status/ship")
    public ResponseEntity<String> shipOrder(@PathVariable int ordersId) {
        log.info("Request to ship orderId: {}", ordersId);
        ordersService.ship(ordersId);
        return ResponseEntity.ok("Order ID " + ordersId + " is now shipping.");
    }

    /**
     * 주문 상태를 '배송 완료'로 변경
     */
    @PatchMapping("/{ordersId}/status/delivered")
    public ResponseEntity<String> markAsDelivered(@PathVariable int ordersId) {
        log.info("Request to mark orderId {} as delivered", ordersId);
        ordersService.markAsDelivered(ordersId);
        return ResponseEntity.ok("Order ID " + ordersId + " has been delivered.");
    }

    /**
     * 주문 상태를 '구매 확정'으로 변경
     */
    @PatchMapping("/{ordersId}/status/confirmed")
    public ResponseEntity<String> confirmPurchase(@PathVariable int ordersId) {
        log.info("Request to confirm purchase for orderId: {}", ordersId);
        ordersService.confirmPurchase(ordersId);
        return ResponseEntity.ok("Order ID " + ordersId + " purchase confirmed.");
    }

    /**
     * 주문에 대한 '취소 요청' 처리
     */
    @PatchMapping("/{ordersId}/status/cancel-requested")
    public ResponseEntity<String> requestCancel(@PathVariable int ordersId) {
        log.info("Request to cancel orderId: {}", ordersId);
        ordersService.requestCancel(ordersId);
        return ResponseEntity.ok("Cancellation request for order ID " + ordersId + " has been submitted.");
    }

    /**
     * 주문을 '취소 완료' 상태로 변경
     */
    @PatchMapping("/{ordersId}/status/cancelled")
    public ResponseEntity<String> cancelOrder(@PathVariable int ordersId) {
        log.info("Confirm cancellation for orderId: {}", ordersId);
        ordersService.cancel(ordersId);
        return ResponseEntity.ok("Order ID " + ordersId + " has been cancelled.");
    }

    /**
     * 주문에 대한 '환불 요청' 처리
     */
    @PatchMapping("/{ordersId}/status/refund-requested")
    public ResponseEntity<String> requestRefund(@PathVariable int ordersId) {
        log.info("Request to refund orderId: {}", ordersId);
        ordersService.requestRefund(ordersId);
        return ResponseEntity.ok("Refund request for order ID " + ordersId + " has been submitted.");
    }

    /**
     * 주문을 '환불 완료' 상태로 변경
     */
    @PatchMapping("/{ordersId}/status/refunded")
    public ResponseEntity<String> completeRefund(@PathVariable int ordersId) {
        log.info("Confirm refund for orderId: {}", ordersId);
        ordersService.completeRefund(ordersId);
        return ResponseEntity.ok("Order ID " + ordersId + " has been refunded.");
    }
}
