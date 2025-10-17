package com.eneifour.fantry.notice.dto;

import com.eneifour.fantry.inquiry.domain.CsStatus;
import com.eneifour.fantry.notice.domain.Notice;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.notice.domain.NoticeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 관리자가 새로운 공지사항을 생성할 때 사용하는 요청 DTO입니다.
 */
public record NoticeCreateRequest(
        @NotNull int noticeTypeId,
        @NotBlank String title,
        @NotBlank String content,
        CsStatus status
) {
    public Notice toEntity(Member admin, NoticeType noticeType, String sanitizedContent) {
        CsStatus newStatus = (status == null) ? CsStatus.DRAFT : status;
        return Notice.builder()
                .noticeType(noticeType)
                .title(title)
                .content(sanitizedContent)
                .createdBy(admin)
                .status(newStatus)
                .build();
    }
}