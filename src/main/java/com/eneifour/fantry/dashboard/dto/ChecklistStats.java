package com.eneifour.fantry.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChecklistStats {
    private final long totalChecklistTemplates;
    private final long totalChecklistItems;
}
