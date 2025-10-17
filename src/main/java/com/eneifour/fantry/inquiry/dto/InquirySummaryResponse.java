package com.eneifour.fantry.inquiry.dto;

import com.eneifour.fantry.inquiry.domain.CsType;
import com.eneifour.fantry.inquiry.domain.Inquiry;
import com.eneifour.fantry.inquiry.domain.InquiryStatus;

import java.time.LocalDateTime;

public record InquirySummaryResponse(
        int inquiryId,
        String title,
        String content,
        String inquiredByName,
        InquiryStatus status,
        LocalDateTime inquiredAt,
        String answerContent,
        String answeredByName,
        String csType
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
                (inquiry.getAnsweredBy() != null) ? inquiry.getAnsweredBy().getName() : null,
                inquiry.getCsType().getName()
        );
    }
}
