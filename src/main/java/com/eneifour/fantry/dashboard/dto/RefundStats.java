package com.eneifour.fantry.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefundStats {
    private final long totalRefunds;
    private final long requestedRefunds;
    private final long inTransitRefunds;
    private final long inspectingRefunds;
    private final long approvedRefunds;
    private final long rejectedRefunds;
    private final long completedRefunds;
    private final long userCancelledRefunds;
}