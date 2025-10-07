package com.eneifour.fantry.cs.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FaqErrorCode {

  // === [404] 리소스 없음 ===
  FAQ_NOT_FOUND(HttpStatus.NOT_FOUND, "FAQ001", "해당 FAQ를 찾을 수 없습니다."),
  // (설명: 주로 관리자가 존재하지 않는 FAQ ID로 수정/삭제를 시도할 때 사용)
  CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "FAQ002", "존재하지 않는 FAQ 카테고리입니다."),
  // (설명: 주로 관리자가 없는 카테고리 ID로 FAQ를 생성/수정할 때 사용)


  // === [400] 잘못된 요청 ===
  // --- 관리자용 에러 ---
  QUESTION_IS_REQUIRED(HttpStatus.BAD_REQUEST, "FAQ003", "질문 내용은 필수입니다."),
  ANSWER_IS_REQUIRED(HttpStatus.BAD_REQUEST, "FAQ004", "답변 내용은 필수입니다."),
  CATEGORY_IS_REQUIRED(HttpStatus.BAD_REQUEST, "FAQ005", "카테고리는 필수입니다."),

  // --- 사용자용 에러 ---
  INVALID_SEARCH_PARAMETER(HttpStatus.BAD_REQUEST, "FAQ007", "유효하지 않은 검색 파라미터입니다."),
  // (설명: 사용자가 존재하지 않는 카테고리 이름 등으로 검색을 시도할 때 사용)


  // === [403] 권한 없음 ===
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "FAQ006", "해당 기능에 대한 접근 권한이 없습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}