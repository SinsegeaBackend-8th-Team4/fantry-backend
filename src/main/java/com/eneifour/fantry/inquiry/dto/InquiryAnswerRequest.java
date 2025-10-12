package com.eneifour.fantry.inquiry.dto;

import com.eneifour.fantry.inquiry.domain.InquiryStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class InquiryAnswerRequest {
    @NotBlank(message = "답변은 필수입니다.")
    private String answerContent;

    private InquiryStatus reqStatus;

    private String comment;
}
