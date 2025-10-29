package com.eneifour.fantry.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class SalesStats {
    private final long totalSalesProducts;
    private final BigDecimal totalSoldAmount;
}
