package com.eneifour.fantry.settlement.dto;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * 관리자 대시보드에 표시될 정산 관련 핵심 지표(KPI) DTO.
 */
@Builder
public record SettlementDashboardResponse(
        BigDecimal monthlyScheduledAmount, // 당월 정산 예정액
        BigDecimal yesterdaySettledAmount, // 어제 정산된 금액
        long pendingOrFailedCount,     // 정산 보류/실패 건수
        BigDecimal cumulativeSettlementAmount // 누적 정산액
) {
}
