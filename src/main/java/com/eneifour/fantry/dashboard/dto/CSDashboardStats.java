package com.eneifour.fantry.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CSDashboardStats {
    private final NoticeStats noticeStats;
    private final FaqStats faqStats;
    private final InquiryStats inquiryStats;
}
