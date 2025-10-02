package com.eneifour.fantry.cs.dto;

import com.eneifour.fantry.cs.domain.CsType;
import com.eneifour.fantry.cs.domain.Inquiry;
import com.eneifour.fantry.cs.domain.InquiryStatus;
import com.eneifour.fantry.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// 문의글 작성용 레코드 DTO (파일제외)
public record InquiryCreateRequest(
    @NotNull(message = "문의 유형은 필수입니다.")
    int csTypeId,

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Size(max = 100, message = "제목은 100자를 넘을 수 없습니다.")
    String title,

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    @Size(max = 2000, message = "내용은 2000자를 넘을 수 없습니다.")
    String content
){
    /***
     * DTO를 엔티리로 변환하는 메서드
     */
    public Inquiry toEntity(Member member, CsType csType){
        return Inquiry.builder()
                .inquiredBy(member)
                .csType(csType)
                .title(this.title)
                .content(this.content)
                .status(InquiryStatus.PENDING)
                .build();
    }
}
