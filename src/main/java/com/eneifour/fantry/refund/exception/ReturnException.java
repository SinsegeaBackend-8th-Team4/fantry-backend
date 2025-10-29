package com.eneifour.fantry.refund.exception;

import lombok.Getter;

/**
 * 환불/반품 도메인에서 발생하는 모든 예외를 나타내는 커스텀 예외 클래스입니다.
 * <p>이 예외는 {@link ReturnErrorCode}를 포함하여, 예외 상황에 대한 구체적인 정보를 전달합니다.
 */
@Getter
public class ReturnException extends RuntimeException {

    private final ReturnErrorCode errorCode;

    public ReturnException(ReturnErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ReturnException(ReturnErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
