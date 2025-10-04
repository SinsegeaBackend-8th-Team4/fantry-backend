package com.eneifour.fantry.cs.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * CS 서비스 에러 코드
 */
@Getter
@RequiredArgsConstructor
public enum CsErrorCode {
    INQUIRY_NOT_FOUND(HttpStatus.NOT_FOUND, "CS001", "해당 문의를 찾을 수 없습니다."),
    CSTYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "CS002", "존재하지 않는 문의 유형입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "CS003", "해당 기능에 대한 접근 권한이 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
