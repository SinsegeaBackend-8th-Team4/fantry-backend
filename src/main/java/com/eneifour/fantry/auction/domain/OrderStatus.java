package com.eneifour.fantry.auction.domain;

public enum OrderStatus {
    PENDING_PAYMENT, //결제 대기중
    PAID, // 결제 완료
    PREPARING_SHIPMENT, //배송 대기중
    SHIPPED, // 배송 중
    DELIVERED, //배송 완료
    CONFIRMED, //구매 확정
    CANCEL_REQUESTED, //취소요청
    CANCELLED, //취소 완료
    REFUND_REQUESTED, //환불 요청
    REFUNDED //환불 완료
}
