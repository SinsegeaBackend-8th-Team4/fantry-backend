package com.eneifour.fantry.inquiry.dto;

import com.eneifour.fantry.inquiry.domain.CsType;
import com.eneifour.fantry.inquiry.domain.Inquiry;
import com.eneifour.fantry.inquiry.domain.InquiryStatus;

import java.time.LocalDateTime;
import java.util.List;

public record InquiryDetailAdminResponse(
        int inquiryId,
        String title,
        String content,
        String inquiredByName,
        InquiryStatus status,
        LocalDateTime inquiredAt,
        String answerContent,
        String answeredByName,
        LocalDateTime answeredAt,
        List<String> attachmentUrls,
        String comment, // 관리자용 메모
        CsType csType
) {
    public static InquiryDetailAdminResponse from(Inquiry inquiry, List<String> urls) {
        return new InquiryDetailAdminResponse(
                inquiry.getInquiryId(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getInquiredBy().getName(),
                inquiry.getStatus(),
                inquiry.getInquiredAt(),
                inquiry.getAnswerContent(),
                (inquiry.getAnsweredBy() != null) ? inquiry.getAnsweredBy().getName() : null,
                inquiry.getAnsweredAt(),
                urls,
                inquiry.getComment(),
                inquiry.getCsType()
        );
    }
}
