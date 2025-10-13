package com.eneifour.fantry.refund.dto;

import com.eneifour.fantry.refund.domain.ReturnReason;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReturnCreateRequest(
        @NotNull int orderId,
        @NotNull ReturnReason reason,
        @Size(max = 2000) String detailReason
) {}
