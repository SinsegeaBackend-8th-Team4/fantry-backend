package com.eneifour.fantry.faq.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FaqErrorCode {

  // === [404] 리소스 없음 ===
  FAQ_NOT_FOUND(HttpStatus.NOT_FOUND, "FAQ001", "해당 FAQ를 찾을 수 없습니다."),
  CSTYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "FAQ002", "존재하지 않는 문의 유형(카테고리)입니다."),

  // === [400] 잘못된 요청 (주로 관리자용) ===
  TITLE_IS_REQUIRED(HttpStatus.BAD_REQUEST, "FAQ003", "제목(질문)은 필수입니다."),
  CONTENT_IS_REQUIRED(HttpStatus.BAD_REQUEST, "FAQ004", "내용(답변)은 필수입니다."),
  CSTYPE_IS_REQUIRED(HttpStatus.BAD_REQUEST, "FAQ005", "문의 유형(카테고리)은 필수입니다."),

  // === [403] 권한 없음 ===
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "FAQ006", "해당 기능에 대한 접근 권한이 없습니다."),

  // === [400] 잘못된 요청 (주로 사용자용) ===
  INVALID_SEARCH_PARAMETER(HttpStatus.BAD_REQUEST, "FAQ007", "유효하지 않은 검색 파라미터입니다.");


  private final HttpStatus status;
  private final String code;
  private final String message;
}