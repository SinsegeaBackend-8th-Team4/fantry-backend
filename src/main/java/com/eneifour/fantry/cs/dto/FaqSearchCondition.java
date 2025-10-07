package com.eneifour.fantry.cs.dto;

import com.eneifour.fantry.cs.domain.CsType;
import lombok.Getter;
import lombok.Setter;

/**
 * FAQ 문의 목록 조회를 위한 동적 검색을 담는 DTO
 */
@Getter
@Setter
public class FaqSearchCondition {
    /**
     * 검색할 FAQ 제목 (부분 일치)
     */
    private String question;

    /**
     * 검색할 FAQ 내용 (부분 일치)
     */
    private String answer;

    /**
     *  검색할 문의 타입
     */
    private CsType csType;

}
