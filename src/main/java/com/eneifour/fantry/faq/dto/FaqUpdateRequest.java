package com.eneifour.fantry.faq.dto;

import com.eneifour.fantry.inquiry.domain.CsStatus;

public record FaqUpdateRequest(
        int csTypeId,
        String title,
        String content,
        CsStatus status
) {
}
