package com.eneifour.fantry.inspection.domain;

import lombok.Getter;

@Getter
public enum InspectionStatus {
    DRAFT("작성 중"),
    SUBMITTED("온라인 검수 제출"),

    // 1차(온라인) 검수 결과
    ONLINE_APPROVED("온라인 검수 승인"),
    ONLINE_REJECTED("온라인 검수 반려"),

    // 2차(오프라인) 검수 과정
    OFFLINE_INSPECTING("오프라인 검수 중"),

    // 2차(오프라인) 검수 결과
    OFFLINE_REJECTED("오프라인 검수 반려"),

    COMPLETED("최종 검수 완료"); // 최종 완료

    private final String label;

    InspectionStatus(String label) {
        this.label = label;
    }
}
