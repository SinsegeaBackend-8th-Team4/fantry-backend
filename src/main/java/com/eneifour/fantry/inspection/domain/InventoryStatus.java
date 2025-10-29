package com.eneifour.fantry.inspection.domain;

import lombok.Getter;

@Getter
public enum InventoryStatus {
    PENDING_REGIST("판매 등록 대기"),
    PENDING_ACTIVE("판매 대기"),
    ACTIVE("판매 중"),
    SOLD("판매 완료"),
    NOT_SOLD("판매 실패"),
    CANCELED("판매 취소"),
    REACTIVE("재판매 중");

    private final String label;

    InventoryStatus(String label) {
        this.label = label;
    }
}
