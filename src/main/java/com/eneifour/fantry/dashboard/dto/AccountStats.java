package com.eneifour.fantry.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountStats {
    private final long totalAccounts;
    private final long activeAccounts;
    private final long inactiveAccounts;
    private final long refundableAccounts;
    private final long nonRefundableAccounts;
}
