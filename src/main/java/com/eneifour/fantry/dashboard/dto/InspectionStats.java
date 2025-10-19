package com.eneifour.fantry.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InspectionStats {
    private final long totalInspections;
    private final long draftInspections;
    private final long submittedInspections;
    private final long onlineApprovedInspections;
    private final long onlineRejectedInspections;
    private final long offlineInspectingInspections;
    private final long offlineRejectedInspections;
    private final long completedInspections;
}