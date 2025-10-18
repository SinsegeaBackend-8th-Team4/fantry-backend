package com.eneifour.fantry.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BidStats {
    private final long totalBids;
    private final long bidsOnActiveAuctions;
    private final long bidsToday;
}
