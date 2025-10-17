package com.eneifour.fantry.refund.dto;

import com.eneifour.fantry.refund.domain.ReturnStatus;

/**
 * 환불 목록 동적 검색을 위한 조건을 담는 DTO입니다.
 */
public record ReturnSearchRequest(
        ReturnStatus status,
        String buyerName
) {}
