package com.eneifour.fantry.orders.domain;


import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.auction.exception.ErrorCode;
import com.eneifour.fantry.orders.exception.OrdersException;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.payment.domain.Payment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private int ordersId;

    private int price;

    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "auction_id")
    private Auction auction;

    @ManyToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;


    /**
     * 결제 완료 처리
     * 결제 대기중(PENDING_PAYMENT)이 아니면 예외 발생
     */
    public void completePayment(String shippingAddress, Payment  payment) {
        if (this.orderStatus != OrderStatus.PENDING_PAYMENT) {
            throw new OrdersException(ErrorCode.ORDER_PAYMENT_NOT_PENDING);
        }
        this.orderStatus = OrderStatus.PAID;
        this.shippingAddress = shippingAddress;
        this.payment = payment;
    }

    /**
     * 배송 준비 시작
     * 결제 완료(PAID)가 아니면 예외 발생
     */
    public void prepareShipment() {
        if (this.orderStatus != OrderStatus.PAID) {
            throw new OrdersException(ErrorCode.ORDER_SHIPMENT_NOT_ALLOWED);
        }
        this.orderStatus = OrderStatus.PREPARING_SHIPMENT;
    }

    /**
     * 배송 시작
     * 배송 준비중(PREPARING_SHIPMENT)이 아니면 예외 발생
     */
    public void ship() {
        if (this.orderStatus != OrderStatus.PREPARING_SHIPMENT) {
            throw new OrdersException(ErrorCode.ORDER_ALREADY_SHIPPED);
        }
        this.orderStatus = OrderStatus.SHIPPED;
    }

    /**
     * 배송 완료 처리
     * 배송 중(SHIPPED)이 아니면 예외 발생
     */
    public void markAsDelivered() {
        if (this.orderStatus != OrderStatus.SHIPPED) {
            throw new OrdersException(ErrorCode.ORDER_DELIVERY_NOT_ALLOWED);
        }
        this.orderStatus = OrderStatus.DELIVERED;
    }

    /**
     * 구매 확정 처리
     * 배송 완료(DELIVERED)가 아니면 예외 발생
     */
    public void confirmPurchase() {
        if (this.orderStatus != OrderStatus.DELIVERED) {
            throw new OrdersException(ErrorCode.ORDER_CONFIRMATION_NOT_ALLOWED);
        }
        this.orderStatus = OrderStatus.CONFIRMED;
    }

    /**
     * 주문 취소 요청
     * 이미 취소되었거나 환불된 주문이면 예외 발생
     */
    public void requestCancel() {
        if (this.orderStatus == OrderStatus.CANCELLED || this.orderStatus == OrderStatus.REFUNDED) {
            throw new OrdersException(ErrorCode.ORDER_ALREADY_CANCELLED);
        }
        this.orderStatus = OrderStatus.CANCEL_REQUESTED;
    }

    /**
     * 주문 취소 확정
     * 취소 요청 상태(CANCEL_REQUESTED)가 아니면 예외 발생
     */
    public void cancel() {
        if (this.orderStatus != OrderStatus.CANCEL_REQUESTED) {
            throw new OrdersException(ErrorCode.ORDER_CANCELLATION_NOT_ALLOWED);
        }
        this.orderStatus = OrderStatus.CANCELLED;
    }

    /**
     * 환불 요청
     * 배송 완료(DELIVERED) 또는 구매 확정(CONFIRMED) 상태가 아니면 예외 발생
     */
    public void requestRefund() {
        if (this.orderStatus != OrderStatus.CONFIRMED && this.orderStatus != OrderStatus.DELIVERED) {
            throw new OrdersException(ErrorCode.ORDER_REFUND_NOT_ALLOWED);
        }
        this.orderStatus = OrderStatus.REFUND_REQUESTED;
    }

    /**
     * 환불 완료 처리
     * 환불 요청(REFUND_REQUESTED)이 아니면 예외 발생
     */
    public void completeRefund() {
        if (this.orderStatus != OrderStatus.REFUND_REQUESTED) {
            throw new OrdersException(ErrorCode.ORDER_INVALID_TRANSITION);
        }
        this.orderStatus = OrderStatus.REFUNDED;
    }

}
