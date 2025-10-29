package com.eneifour.fantry.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoticeStats {
    private final long totalNotices;
    private final long draftNotices;
    private final long activeNotices;
    private final long pinnedNotices;
    private final long inactiveNotices;
}