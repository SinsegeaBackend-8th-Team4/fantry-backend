package com.eneifour.fantry.dashboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 관리자 대시보드 요약 정보를 담는 전체 응답 DTO.
 * 다른 도메인의 통계(e.g., RefundStats)가 추가될 수 있도록 구조화됨.
 */
@Schema(description = "관리자 대시보드 전체 요약 정보 응답 DTO")
public record DashboardSummaryResponse(
        @Schema(description = "문의 관련 통계")
        InquiryStats inquiryStats
        // 나중에 여기에 refundStats, settlementStats 등이 추가될 수 있음.
) {
}
