package com.eneifour.fantry.faq.dto;

import com.eneifour.fantry.inquiry.domain.CsStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "관리자용 FAQ 도메인 상세 통계 응답 DTO")
public record FaqStatsAdminResponse(
        @Schema(description = "임시저장 상태의 FAQ 건수", example = "2")
        long draft,
        @Schema(description = "활성 상태의 FAQ 건수", example = "25")
        long active,
        @Schema(description = "상단고정 상태의 FAQ 건수", example = "3")
        long pinned,
        @Schema(description = "비활성 상태의 FAQ 건수", example = "5")
        long inactive,
        @Schema(description = "전체 FAQ 건수", example = "35")
        long total
) {
    public static FaqStatsAdminResponse from(Map<CsStatus, Long> statusCounts) {
        long draft = statusCounts.getOrDefault(CsStatus.DRAFT, 0L);
        long active = statusCounts.getOrDefault(CsStatus.ACTIVE, 0L);
        long pinned = statusCounts.getOrDefault(CsStatus.PINNED, 0L);
        long inactive = statusCounts.getOrDefault(CsStatus.INACTIVE, 0L);
        long total = draft + active + pinned + inactive;

        return new FaqStatsAdminResponse(draft, active, pinned, inactive, total);
    }
}
