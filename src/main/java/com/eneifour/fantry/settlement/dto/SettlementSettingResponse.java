package com.eneifour.fantry.settlement.dto;

import com.eneifour.fantry.settlement.domain.SettlementCycleType;
import com.eneifour.fantry.settlement.domain.SettlementSetting;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 정산 설정 조회 응답 DTO.
 */
@Builder
public record SettlementSettingResponse(
        int settlementSettingId,
        BigDecimal commissionRate,
        SettlementCycleType settlementCycleType,
        Integer settlementDay,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy
) {
    public static SettlementSettingResponse from(SettlementSetting setting) {
        String createdByName = (setting.getCreatedBy() != null) ? setting.getCreatedBy().getName() : null;
        String updatedByName = (setting.getUpdatedBy() != null) ? setting.getUpdatedBy().getName() : null;

        return SettlementSettingResponse.builder()
                .settlementSettingId(setting.getSettlementSettingId())
                .commissionRate(setting.getCommissionRate())
                .settlementCycleType(setting.getSettlementCycleType())
                .settlementDay(setting.getSettlementDay())
                .createdAt(setting.getCreatedAt())
                .createdBy(createdByName)
                .updatedAt(setting.getUpdatedAt())
                .updatedBy(updatedByName)
                .build();
    }
}
