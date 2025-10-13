package com.eneifour.fantry.settlement.dto;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.settlement.domain.SettlementCycleType;
import com.eneifour.fantry.settlement.domain.SettlementSetting;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * 정산 설정 생성 및 수정 요청 DTO.
 */
public record SettlementSettingRequest(
        @NotNull(message = "수수료율은 필수입니다.")
        @DecimalMin(value = "0.0", message = "수수료율은 0 이상이어야 합니다.")
        @DecimalMax(value = "100.0", message = "수수료율은 100 이하여야 합니다.")
        BigDecimal commissionRate,

        @NotNull(message = "정산 주기는 필수입니다.")
        SettlementCycleType settlementCycleType,

        Integer settlementDay
) {
    public SettlementSetting toEntity(Member admin) {
        return SettlementSetting.builder()
                .commissionRate(this.commissionRate)
                .settlementCycleType(this.settlementCycleType)
                .settlementDay(this.settlementDay)
                .createdBy(admin)
                .updatedBy(admin)
                .build();
    }
}