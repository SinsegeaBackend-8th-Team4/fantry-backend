package com.eneifour.fantry.cs.dto;

import com.eneifour.fantry.cs.domain.Inquiry;
import com.eneifour.fantry.cs.domain.InquiryStatus;

import java.time.LocalDateTime;
import java.util.List;

public record InquiryDetailResponse(
        int inquiryId,
        String title,
        String content,
        String inquiredByName,
        InquiryStatus status,
        LocalDateTime inquiredAt,
        String answerContent,
        String answeredByName,
        LocalDateTime answeredAt,
        List<String> attachmentUrls
) {
    public static InquiryDetailResponse from(Inquiry inquiry, List<String> urls) {
        return new InquiryDetailResponse(
                inquiry.getInquiryId(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getInquiredBy().getName(),
                inquiry.getStatus(),
                inquiry.getInquiredAt(),
                inquiry.getAnswerContent(),
                (inquiry.getAnsweredBy() != null) ? inquiry.getAnsweredBy().getName() : null,
                inquiry.getAnsweredAt(),
                urls
        );
    }
}
