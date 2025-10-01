package com.eneifour.fantry.payment.exception;

import lombok.Getter;

/**
 * Payment 예외처리
 */
@Getter
public class BootpayException extends RuntimeException {
    private final PaymentErrorCode errorCode;

    public BootpayException(PaymentErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BootpayException(PaymentErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
