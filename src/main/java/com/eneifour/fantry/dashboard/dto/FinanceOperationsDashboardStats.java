package com.eneifour.fantry.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FinanceOperationsDashboardStats {
    private final SettlementStats settlementStats;
    private final RefundStats refundStats;
}
