package com.eneifour.fantry.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SettlementStats {
    private final long totalSettlements;
    private final long pendingSettlements;
    private final long paidSettlements;
    private final long cancelledSettlements;
    private final long failedSettlements;
}