package com.eneifour.fantry.notice.dto;

import com.eneifour.fantry.inquiry.domain.CsStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "관리자용 공지사항 도메인 상세 통계 응답 DTO")
public record NoticeStatsAdminResponse(
        @Schema(description = "임시저장 상태의 공지사항 건수", example = "1")
        long draft,
        @Schema(description = "활성 상태의 공지사항 건수", example = "15")
        long active,
        @Schema(description = "상단고정 상태의 공지사항 건수", example = "2")
        long pinned,
        @Schema(description = "비활성 상태의 공지사항 건수", example = "3")
        long inactive,
        @Schema(description = "전체 공지사항 건수", example = "21")
        long total
) {
    public static NoticeStatsAdminResponse from(Map<CsStatus, Long> statusCounts) {
        long draft = statusCounts.getOrDefault(CsStatus.DRAFT, 0L);
        long active = statusCounts.getOrDefault(CsStatus.ACTIVE, 0L);
        long pinned = statusCounts.getOrDefault(CsStatus.PINNED, 0L);
        long inactive = statusCounts.getOrDefault(CsStatus.INACTIVE, 0L);
        long total = draft + active + pinned + inactive;

        return new NoticeStatsAdminResponse(draft, active, pinned, inactive, total);
    }
}
