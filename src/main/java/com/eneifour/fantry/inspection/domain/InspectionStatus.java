package com.eneifour.fantry.inspection.domain;

import lombok.Getter;

@Getter
public enum InspectionStatus {
    DRAFT("작성 중"),
    SUBMITTED("1차 제출"),
    FIRST_REVIEWED("1차 승인"),
    OFFLINE_INSPECTING("2차 검수 중"),
    COMPLETED("검수 완료"),
    REJECTED("반려");

    private final String label;

    InspectionStatus(String label) {
        this.label = label;
    }
}
