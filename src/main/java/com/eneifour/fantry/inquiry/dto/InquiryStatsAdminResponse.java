package com.eneifour.fantry.inquiry.dto;

import com.eneifour.fantry.inquiry.domain.InquiryStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * (신규) 관리자용 문의 도메인 상세 통계 응답 DTO.
 * 각 상태별 문의 건수를 포함한다.
 */
@Schema(description = "관리자용 문의 도메인 상세 통계 응답 DTO")
public record InquiryStatsAdminResponse(
        @Schema(description = "답변 대기 중인 총 문의 건수", example = "12")
        long pending,
        @Schema(description = "처리 중인 총 문의 건수", example = "3")
        long inProgress,
        @Schema(description = "보류 중인 총 문의 건수", example = "1")
        long onHold,
        @Schema(description = "답변 완료된 총 문의 건수", example = "150")
        long answered,
        @Schema(description = "거절된 총 문의 건수", example = "5")
        long rejected,
        @Schema(description = "전체 문의 건수", example = "171")
        long total
) {
    public static InquiryStatsAdminResponse from(Map<InquiryStatus, Long> statusCounts) {
        long pending = statusCounts.getOrDefault(InquiryStatus.PENDING, 0L);
        long inProgress = statusCounts.getOrDefault(InquiryStatus.IN_PROGRESS, 0L);
        long onHold = statusCounts.getOrDefault(InquiryStatus.ON_HOLD, 0L);
        long answered = statusCounts.getOrDefault(InquiryStatus.ANSWERED, 0L);
        long rejected = statusCounts.getOrDefault(InquiryStatus.REJECTED, 0L);
        long total = pending + inProgress + onHold + answered + rejected;

        return new InquiryStatsAdminResponse(pending, inProgress, onHold, answered, rejected, total);
    }
}