package com.eneifour.fantry.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode {
    //회원과 관련된 오류
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "존재하지 않는 회원입니다."),
    MEMBER_ID_DUPLICATED(HttpStatus.CONFLICT, "M002", "이미 사용 중인 아이디입니다."),
    MEMBER_EMAIL_DUPLICATED(HttpStatus.CONFLICT, "M003", "이미 사용 중인 이메일입니다."),
    MEMBER_ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "M004", "요청하신 역할 정보(Role)를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND_BY_EMAIL(HttpStatus.NOT_FOUND, "M005", "해당 이메일로 등록된 회원이 없습니다."),
    MEMBER_INVALID_PASSWORD(HttpStatus.NOT_FOUND, "M006", "2차 비밀번호가 유효하지 않습니다."),

    //권한과 관련된 오류
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "M101", "존재하지 않는 권한입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
