package com.eneifour.fantry.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InquiryStats {
    private final long totalInquiries;
    private final long pendingInquiries;
    private final long inProgressInquiries;
    private final long onHoldInquiries;
    private final long rejectedInquiries;
    private final long answeredInquiries;
    private final long todayInquiries;
}