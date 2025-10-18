package com.eneifour.fantry.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportStats {
    private final long totalReports;
    private final long resolvedReports;
    private final long receivedReports;
    private final long withdrawnReports;
    private final long rejectedReports;
}
