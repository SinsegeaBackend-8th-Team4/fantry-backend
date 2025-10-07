package com.eneifour.fantry.cs.dto;

import com.eneifour.fantry.cs.domain.CsStatus;
import com.eneifour.fantry.cs.domain.Faq;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public record FaqResponse(
        int faqId,
        String csType,
        String title,
        String content,
        CsStatus status, // [추가!]
        String createdBy,
        LocalDateTime createdAt,
        String modifiedBy,
        LocalDateTime updatedAt,
        List<String> attachmentUrls
) {
    public static FaqResponse from(Faq faq) {
        return from(faq, Collections.emptyList());
    }

    public static FaqResponse from(Faq faq, List<String> attachmentUrls) {
        String createdBy = (faq.getCreatedBy() != null) ? faq.getCreatedBy().getName() : null;
        String modifiedBy = (faq.getUpdatedBy() != null) ? faq.getUpdatedBy().getName() : null;

        return new FaqResponse(
                faq.getFaqId(),
                faq.getCsType().getName(),
                faq.getTitle(),
                faq.getContent(),
                faq.getStatus(), // [추가!]
                createdBy,
                faq.getCreatedAt(),
                modifiedBy,
                faq.getUpdatedAt(),
                attachmentUrls
        );
    }
}
