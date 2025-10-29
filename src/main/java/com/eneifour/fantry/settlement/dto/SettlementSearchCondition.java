package com.eneifour.fantry.settlement.dto;

import com.eneifour.fantry.settlement.domain.SettlementStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 정산 내역 목록 조회를 위한 동적 검색 조건을 담는 DTO.
 */
@Getter
@Setter
public class SettlementSearchCondition {

    private String sellerName;

    private SettlementStatus status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
}
