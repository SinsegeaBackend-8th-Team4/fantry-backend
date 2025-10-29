package com.eneifour.fantry.refund.domain;

public enum ReturnStatus {
    REQUESTED,
    IN_TRANSIT,
    INSPECTING,
    APPROVED,
    REJECTED,
    COMPLETED,
    USER_CANCELLED,
    DELETED
}
