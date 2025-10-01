package com.eneifour.fantry.payment.exception;

import lombok.Getter;

@Getter
public class PaymentConfirmException extends RuntimeException {
    private final PaymentErrorCode errorCode;

    public PaymentConfirmException() {
        super(PaymentErrorCode.PAYMENT_CONFIRM_FAILED.getMessage());
        this.errorCode = PaymentErrorCode.PAYMENT_CONFIRM_FAILED;
    }

    public PaymentConfirmException(Throwable cause) {
        super(PaymentErrorCode.PAYMENT_CONFIRM_FAILED.getMessage(), cause);
        this.errorCode = PaymentErrorCode.PAYMENT_CONFIRM_FAILED;
    }
}
