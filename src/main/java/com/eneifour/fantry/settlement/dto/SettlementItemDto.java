package com.eneifour.fantry.settlement.dto;

import com.eneifour.fantry.settlement.domain.SettlementItem;
import lombok.Builder;

import java.math.BigDecimal;

/**
 * 정산 상세 내역에 포함되는 개별 정산 항목 DTO.
 */
@Builder
public record SettlementItemDto(
        int orderId,
        String productName,
        BigDecimal itemSaleAmount,
        BigDecimal commissionRate,
        BigDecimal commissionAmount,
        BigDecimal totalAmount,
        boolean isReturned
) {
    public static SettlementItemDto from(SettlementItem item) {
        String productName = "N/A";
        if (item.getOrder() != null && item.getOrder().getAuction() != null && item.getOrder().getAuction().getProductInspection() != null) {
            productName = item.getOrder().getAuction().getProductInspection().getItemName();
        }

        return SettlementItemDto.builder()
                .orderId(item.getOrder() != null ? item.getOrder().getOrdersId() : 0)
                .productName(productName)
                .itemSaleAmount(item.getItemSaleAmount())
                .commissionRate(item.getCommissionRate())
                .commissionAmount(item.getCommissionAmount())
                .totalAmount(item.getTotalAmount())
                .isReturned(item.isReturned())
                .build();
    }
}
