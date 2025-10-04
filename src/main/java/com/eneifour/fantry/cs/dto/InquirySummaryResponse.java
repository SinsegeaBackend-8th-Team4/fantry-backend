package com.eneifour.fantry.cs.dto;

import com.eneifour.fantry.cs.domain.Inquiry;
import com.eneifour.fantry.cs.domain.InquiryStatus;

import java.time.LocalDateTime;

public record InquirySummaryResponse(
        int inquiryId,
        String title,
        String content,
        String inquiredByName,
        InquiryStatus status,
        LocalDateTime inquiredAt,
        String answerContent,
        String answeredByName
) {
    public static InquirySummaryResponse from(Inquiry inquiry) {
        return new InquirySummaryResponse(
                inquiry.getInquiryId(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getInquiredBy().getName(),
                inquiry.getStatus(),
                inquiry.getInquiredAt(),
                inquiry.getAnswerContent(),
                (inquiry.getAnsweredBy() != null) ? inquiry.getAnsweredBy().getName() : null
        );
    }
}
