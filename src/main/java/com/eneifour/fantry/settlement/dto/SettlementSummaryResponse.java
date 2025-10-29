package com.eneifour.fantry.settlement.dto;

import com.eneifour.fantry.settlement.domain.Settlement;
import com.eneifour.fantry.settlement.domain.SettlementStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 정산 내역 목록 조회의 응답 DTO.
 */
@Builder
public record SettlementSummaryResponse(
        int settlementId,
        String sellerName,
        BigDecimal settlementAmount,
        SettlementStatus status,
        LocalDateTime requestedAt,
        LocalDateTime completedAt
) {
    public static SettlementSummaryResponse from(Settlement settlement) {
        return SettlementSummaryResponse.builder()
                .settlementId(settlement.getSettlementId())
                .sellerName(settlement.getMember() != null ? settlement.getMember().getName() : "N/A")
                .settlementAmount(settlement.getSettlementAmount())
                .status(settlement.getStatus())
                .requestedAt(settlement.getRequestedAt())
                .completedAt(settlement.getCompletedAt())
                .build();
    }
}
