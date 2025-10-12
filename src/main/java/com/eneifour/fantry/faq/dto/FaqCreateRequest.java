package com.eneifour.fantry.faq.dto;

import com.eneifour.fantry.inquiry.domain.CsStatus;
import com.eneifour.fantry.inquiry.domain.CsType;
import com.eneifour.fantry.faq.domain.Faq;
import com.eneifour.fantry.member.domain.Member;

public record FaqCreateRequest(
        int csTypeId,
        String title,
        String content
) {
    public Faq toEntity(Member admin, CsType csType, String sanitizedContent) {
        return Faq.builder()
                .csType(csType)
                .title(title)
                .content(sanitizedContent)
                .createdBy(admin)
                .status(CsStatus.ACTIVE) // 생성 시 기본상태는 '공개' 상태
                .build();
    }
}
