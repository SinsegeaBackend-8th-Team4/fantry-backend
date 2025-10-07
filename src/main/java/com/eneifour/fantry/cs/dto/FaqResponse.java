package com.eneifour.fantry.cs.dto;

import com.eneifour.fantry.cs.domain.*;

import java.time.LocalDateTime;

public record FaqResponse(
        int faqId,
        CsType csType,
        String question,
        String answer,
        CsStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static FaqResponse from(Faq faq) {
        return new FaqResponse(
                faq.getFaqId(),
                faq.getCsType(),
                faq.getQuestion(),
                faq.getAnswer(),
                faq.getStatus(),
                faq.getCreatedAt(),
                faq.getUpdatedAt()
        );
    }
}
