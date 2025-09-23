package com.eneifour.fantry.saleItem.domain;

public enum OrderStatus {
    PENDING_PAYMENT,
    PAID,
    PREPARING_SHIPMENT,
    SHIPPED,
    DELIVERED,
    CONFIRMED,
    CANCEL_REQUESTED,
    CANCELLED,
    REFUND_REQUESTED,
    REFUNDED
}
