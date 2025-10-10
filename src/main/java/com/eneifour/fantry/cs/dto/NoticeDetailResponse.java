package com.eneifour.fantry.cs.dto;

import com.eneifour.fantry.cs.domain.CsStatus;
import com.eneifour.fantry.cs.domain.Notice;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 공지사항 상세 조회 시 사용될 응답 DTO입니다. (스마트에디터 내용 포함)
 */
public record NoticeDetailResponse(
        int noticeId,
        String csType,
        String title,
        String content,
        CsStatus status,
        String createdBy,
        LocalDateTime createdAt,
        String updatedBy,
        LocalDateTime updatedAt,
        List<String> attachmentUrls
) {
    public static NoticeDetailResponse from(Notice notice) {
        return from(notice, Collections.emptyList());
    }

    public static NoticeDetailResponse from(Notice notice, List<String> attachmentUrls) {
        String createdBy = (notice.getCreatedBy() != null) ? notice.getCreatedBy().getName() : null;
        String updatedBy = (notice.getUpdatedBy() != null) ? notice.getUpdatedBy().getName() : null;

        return new NoticeDetailResponse(
                notice.getNoticeId(),
                notice.getCsType().getName(),
                notice.getTitle(),
                notice.getContent(),
                notice.getStatus(),
                createdBy,
                notice.getCreatedAt(),
                updatedBy,
                notice.getUpdatedAt(),
                attachmentUrls
        );
    }
}
