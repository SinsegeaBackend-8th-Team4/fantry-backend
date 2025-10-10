package com.eneifour.fantry.cs.dto;

import com.eneifour.fantry.cs.domain.CsStatus;

public record FaqUpdateRequest(
        int csTypeId,
        String title,
        String content,
        CsStatus status
) {
}
