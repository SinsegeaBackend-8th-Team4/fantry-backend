package com.eneifour.fantry.refund.dto;

import com.eneifour.fantry.refund.domain.ReturnStatus;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ReturnAdminUpdateRequest(
        @NotNull ReturnStatus status,
        BigDecimal deductedShippingFee,
        String rejectReason,
        String memo
) {}