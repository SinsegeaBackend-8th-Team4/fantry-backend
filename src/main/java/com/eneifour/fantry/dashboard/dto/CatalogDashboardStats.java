package com.eneifour.fantry.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CatalogDashboardStats {
    private final CatalogStats catalogStats;
    private final ChecklistStats checklistStats;
}
