package com.eneifour.fantry.dashboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "FAQ 관련 대시보드 통계 DTO")
public record FaqStats(
        @Schema(description = "활성 상태인 FAQ 건수", example = "25")
        long active,
        @Schema(description = "비활성 상태인 FAQ 건수", example = "5")
        long inactive,
        @Schema(description = "임시저장 상태인 FAQ 건수", example = "2")
        long draft
) {
}
