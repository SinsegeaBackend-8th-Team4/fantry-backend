package com.eneifour.fantry.cs.exception;

import lombok.Getter;

/**
 * CS 서비스 예외처리
 */
@Getter
public class CsException extends RuntimeException {

    private final CsErrorCode errorCode;

    public CsException(CsErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CsException(CsErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
