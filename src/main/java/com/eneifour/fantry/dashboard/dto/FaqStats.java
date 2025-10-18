package com.eneifour.fantry.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FaqStats {
    private final long totalFaqs;
    private final long draftFaqs;
    private final long activeFaqs;
    private final long pinnedFaqs;
    private final long inactiveFaqs;
}