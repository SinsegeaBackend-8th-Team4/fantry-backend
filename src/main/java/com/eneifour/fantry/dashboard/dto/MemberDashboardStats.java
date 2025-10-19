package com.eneifour.fantry.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDashboardStats {
    private final MemberStats memberStats;
    private final ReportStats reportStats;
}
