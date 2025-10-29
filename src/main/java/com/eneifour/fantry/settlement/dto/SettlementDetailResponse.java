package com.eneifour.fantry.settlement.dto;

import com.eneifour.fantry.settlement.domain.Settlement;
import com.eneifour.fantry.settlement.domain.SettlementStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 정산 내역 상세 조회의 응답 DTO.
 */
@Builder
public record SettlementDetailResponse(
        int settlementId,
        String sellerName,
        BigDecimal totalAmount,
        BigDecimal commissionAmount,
        BigDecimal settlementAmount,
        SettlementStatus status,
        LocalDateTime requestedAt,
        LocalDateTime completedAt,
        String failureReason,
        String bankName,
        String accountNumber,
        List<SettlementItemDto> items
) {
    public static SettlementDetailResponse from(Settlement settlement) {
        List<SettlementItemDto> itemDtos = settlement.getSettlementItems().stream()
                .map(SettlementItemDto::from)
                .collect(Collectors.toList());

        String bankName = "N/A";
        String accountNumber = "N/A";
        if (settlement.getAccount() != null) {
            bankName = settlement.getAccount().getBankName();
            accountNumber = settlement.getAccount().getAccountNumber();
        }

        return SettlementDetailResponse.builder()
                .settlementId(settlement.getSettlementId())
                .sellerName(settlement.getMember() != null ? settlement.getMember().getName() : "N/A")
                .totalAmount(settlement.getTotalAmount())
                .commissionAmount(settlement.getCommissionAmount())
                .settlementAmount(settlement.getSettlementAmount())
                .status(settlement.getStatus())
                .requestedAt(settlement.getRequestedAt())
                .completedAt(settlement.getCompletedAt())
                .failureReason(settlement.getFailureReason())
                .bankName(bankName)
                .accountNumber(accountNumber)
                .items(itemDtos)
                .build();
    }
}
