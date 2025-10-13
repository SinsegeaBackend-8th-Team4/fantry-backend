package com.eneifour.fantry.inquiry.exception;

import lombok.Getter;

/**
 * CS 서비스 예외처리
 */
@Getter
public class InquiryException extends RuntimeException {

    private final InquiryErrorCode errorCode;

    public InquiryException(InquiryErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public InquiryException(InquiryErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
