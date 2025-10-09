package com.eneifour.fantry.cs.dto;

import com.eneifour.fantry.cs.domain.CsStatus;

/**
 * 공지사항 동적 검색을 위한 조건을 담는 DTO입니다.
 */
public record NoticeSearchRequest(
        Integer csTypeId,
        String keyword,
        CsStatus status
) {}
