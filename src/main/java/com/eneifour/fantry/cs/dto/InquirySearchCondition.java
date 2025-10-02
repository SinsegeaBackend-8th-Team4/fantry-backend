package com.eneifour.fantry.cs.dto;

import com.eneifour.fantry.cs.domain.CsType;
import com.eneifour.fantry.cs.domain.InquiryStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * 1:1 문의 목록 조회를 위한 동적 검색 조건을 담는 DTO입니다.
 * 컨트롤러에서 @ModelAttribute를 통해 클라이언트의 쿼리 파라미터를 이 객체에 바인딩합니다.
 * 예: /api/inquiries?status=PENDING&memberName=홍길동
 */
@Getter
@Setter
public class InquirySearchCondition {

    /**
     * 검색할 문의 답변 상태 (PENDING, COMPLETED)
     */
    private InquiryStatus status;

    /**
     * 검색할 문의 유형 (결제, 계정, 기타 등)
     */
    private CsType csType;

    /**
     * 검색할 작성자 이름
     */
    private String memberName;
}
