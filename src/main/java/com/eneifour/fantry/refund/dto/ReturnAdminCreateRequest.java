package com.eneifour.fantry.refund.dto;

import com.eneifour.fantry.refund.domain.ReturnReason;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReturnAdminCreateRequest(
        @NotBlank String orderId,
        @NotNull int memberId,
        @NotNull ReturnReason reason,
        String detailReason
) {}
