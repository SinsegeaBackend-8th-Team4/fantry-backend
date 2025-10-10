package com.eneifour.fantry.cs.exception;

import lombok.Getter;

/**
 * 노티스 예외처리
 */
@Getter
public class NoticeException extends RuntimeException {

    private final NoticeErrorCode errorCode;

    public NoticeException(NoticeErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public NoticeException(NoticeErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
