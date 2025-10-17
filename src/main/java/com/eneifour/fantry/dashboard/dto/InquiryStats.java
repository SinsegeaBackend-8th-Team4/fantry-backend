package com.eneifour.fantry.dashboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 문의 관련 대시보드 통계 데이터를 담는 DTO.
 */
@Schema(description = "문의 관련 대시보드 통계 DTO")
public record InquiryStats(
        @Schema(description = "오늘 접수된 문의 건수", example = "5")
        long todayNew,
        @Schema(description = "답변 대기 중인 총 문의 건수", example = "12")
        long pending,
        @Schema(description = "처리 중인 총 문의 건수", example = "3")
        long inProgress
) {
}
