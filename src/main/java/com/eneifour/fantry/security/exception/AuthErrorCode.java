package com.eneifour.fantry.security.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode {
    // 1. 로그인/인증 실패 (UNAUTHORIZED)
    TOKEN_AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "T001", "아이디 또는 비밀번호가 일치하지 않습니다."),
    TOKEN_USER_NOT_NOT_FOUND(HttpStatus.UNAUTHORIZED, "T002", "일치하는 회원이 존재하지 않습니다."), // 로그인/인증 실패 맥락
    TOKEN_ACCESS_DENIED(HttpStatus.FORBIDDEN, "T003", "권한이 없습니다."),

    // 2. JWT 구조 및 형식 오류 (UNAUTHORIZED)
    TOKEN_HEADER_INVALID(HttpStatus.UNAUTHORIZED, "T101", "Authorization 헤더가 없거나 혹은 잘못된 형식입니다."),
    TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "T102", "토큰 정보가 누락되었습니다."),
    TOKEN_UNSUPPORTED_FORMAT(HttpStatus.UNAUTHORIZED, "T110", "지원하지 않는 JWT 형식입니다."),
    TOKEN_MALFORMED_FORMAT(HttpStatus.UNAUTHORIZED, "T111", "잘못된 JWT 형식입니다."),
    TOKEN_SIGNATURE_INVALID(HttpStatus.UNAUTHORIZED, "T112", "JWT 서명 검증에 실패하였습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "T113", "유효하지 않은 토큰입니다."), // 기타 JWT 오류
    TOKEN_NOT_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "T114", "Access Token이 아닙니다."),

    // 3. 토큰 유효성 오류 (UNAUTHORIZED)
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "T201", "AccessToken이 만료되었습니다."), // AccessToken이 만료됨/토큰 시간이 만료됨
    TOKEN_BLACKLISTED(HttpStatus.UNAUTHORIZED, "T202", "해당 토큰은 사용이 금지되어 있습니다."),

    // 4. Refresh Token 오류 (NOT_FOUND / UNAUTHORIZED)
    REFRESH_TOKEN_MISSING(HttpStatus.NOT_FOUND, "T301", "리프레시 토큰이 존재하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "T302", "리프레시 토큰이 만료되었습니다."),
    REFRESH_TOKEN_NOT_IN_STORE(HttpStatus.NOT_FOUND, "T303", "Redis에 저장된 리프레시 토큰이 없습니다."), // 저장된 토큰이 없음
    REFRESH_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "T304", "저장된 토큰과 일치하지 않습니다."),
    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "T305", "유효하지 않은 리프레시 토큰입니다."),
    REFRESH_TOKEN_NOT_SAVE(HttpStatus.UNAUTHORIZED, "T306", "저장된 리프레시 토큰이 없습니다."),

    // 5. 기타 인증 과정 오류 (BAD_REQUEST / NOT_FOUND)
    AUTH_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "T401", "인증 코드가 존재하지 않습니다."),
    AUTH_EMAIL_MISMATCH(HttpStatus.BAD_REQUEST, "T402", "이메일이 일치하지 않습니다."),
    AUTH_NOT_LOGIN_REQUEST(HttpStatus.BAD_REQUEST, "T403", "로그인 요청이 아닙니다."),
    AUTH_VERSION_MISMATCH(HttpStatus.BAD_REQUEST, "T404", "버전이 일치하지 않습니다."),
    AUTH_UNEXPECTED_ERROR(HttpStatus.UNAUTHORIZED, "T499", "인증 에러가 발생했습니다. (기타 예외)");

    private final HttpStatus status;
    private final String message;
    private final String code;
}
