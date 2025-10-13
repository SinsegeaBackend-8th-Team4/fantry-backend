package com.eneifour.fantry.faq.dto;

import com.eneifour.fantry.inquiry.domain.CsStatus;

public record FaqSearchRequest(
        Integer csTypeId, // 카테고리 ID로 검색
        String keyword ,  // 제목 또는 내용에 포함된 키워드
        CsStatus status
) {}