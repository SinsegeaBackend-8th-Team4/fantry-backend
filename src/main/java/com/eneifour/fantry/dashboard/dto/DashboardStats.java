package com.eneifour.fantry.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DashboardStats {
    private final long totalUsers;
    private final long todayRegisteredUsers;
    private final long totalAuctions;
    private final long ongoingAuctions;
    private final long totalInquiries;
    private final long unansweredInquiries;
    private final long totalPayments;
    private final long todayPayments;
    private final long totalSettlements;
    private final long pendingSettlements;
    private final long totalRefunds;
    private final long requestedRefunds;
    private final long totalNotices;
    private final long activeNotices;
    private final long totalFaqs;
    private final long activeFaqs;
    private final long totalBids;
    private final long bidsToday;
    private final long totalAccounts;
    private final long activeAccounts;
    private final long totalArtists;
    private final long approvedArtists;
    private final long totalInspections;
    private final long submittedInspections;
    private final long totalChecklistTemplates;
    private final long totalReports;
    private final long receivedReports;
}
