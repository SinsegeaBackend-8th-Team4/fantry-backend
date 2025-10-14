package com.eneifour.fantry.settlement.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 정산 주기 유형을 나타내는 Enum.
 */
@Getter
@RequiredArgsConstructor
public enum SettlementCycleType {
    DAILY("매일 정산"),
    WEEKLY("매주 정산"),
    MONTHLY("매월 정산"),
    END_OF_MONTH("월말 정산");

    private final String description;
}
