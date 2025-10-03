package com.eneifour.fantry.account.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AccountErrorCode {
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "AC001", "존재하지 않는 계좌입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
