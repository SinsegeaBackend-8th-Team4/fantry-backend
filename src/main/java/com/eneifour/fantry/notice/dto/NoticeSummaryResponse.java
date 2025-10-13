package com.eneifour.fantry.notice.dto;

import com.eneifour.fantry.inquiry.domain.CsStatus;
import com.eneifour.fantry.notice.domain.Notice;
import java.time.LocalDateTime;

/**
 * 공지사항 목록 조회 시 사용될 가벼운 응답 DTO입니다. (상세 내용 제외)
 */
public record NoticeSummaryResponse(
        int noticeId,
        String csType,
        String title,
        CsStatus status,
        String createdBy,
        LocalDateTime createdAt
) {
    public static NoticeSummaryResponse from(Notice notice) {
        String createdBy = (notice.getCreatedBy() != null) ? notice.getCreatedBy().getName() : null;
        return new NoticeSummaryResponse(
                notice.getNoticeId(),
                notice.getCsType().getName(),
                notice.getTitle(),
                notice.getStatus(),
                createdBy,
                notice.getCreatedAt()
        );
    }
}
