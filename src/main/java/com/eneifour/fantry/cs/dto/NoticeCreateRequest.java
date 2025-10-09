package com.eneifour.fantry.cs.dto;

import com.eneifour.fantry.cs.domain.CsStatus;
import com.eneifour.fantry.cs.domain.CsType;
import com.eneifour.fantry.cs.domain.Notice;
import com.eneifour.fantry.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 관리자가 새로운 공지사항을 생성할 때 사용하는 요청 DTO입니다.
 */
public record NoticeCreateRequest(
        @NotNull int csTypeId,
        @NotBlank String title,
        @NotBlank String content
) {
    public Notice toEntity(Member admin, CsType csType, String sanitizedContent) {
        return Notice.builder()
                .csType(csType)
                .title(title)
                .content(sanitizedContent)
                .createdBy(admin)
                .status(CsStatus.ACTIVE)
                .build();
    }
}