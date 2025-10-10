package com.eneifour.fantry.cs.exception;

import lombok.Getter;

/**
 * CS 서비스 예외처리
 */
@Getter
public class FaqException extends RuntimeException {

    private final FaqErrorCode errorCode;

    public FaqException(FaqErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public FaqException(FaqErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
