package com.eneifour.fantry.notice.dto;

import com.eneifour.fantry.inquiry.domain.CsStatus;

public record NoticeSearchRequest(
        Integer csTypeId,
        String keyword,
        CsStatus status
) {}