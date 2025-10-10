package com.eneifour.fantry.inspection.domain;

import lombok.Getter;

@Getter
public enum InventoryStatus {
    PENDING("등록 대기"),
    ACTIVE("판매 중"),
    SOLD("판매 완료"),
    CANCELED("판매 취소");

    private final String label;

    InventoryStatus(String label) {
        this.label = label;
    }
}
