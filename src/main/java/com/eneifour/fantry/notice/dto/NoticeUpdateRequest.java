package com.eneifour.fantry.notice.dto;

import com.eneifour.fantry.inquiry.domain.CsStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 관리자가 기존 공지사항을 수정할 때 사용하는 요청 DTO입니다.
 */
public record NoticeUpdateRequest(
        @NotNull int noticeTypeId,
        @NotBlank String title,
        @NotBlank String content,
        CsStatus status
) {}