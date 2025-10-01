package com.eneifour.fantry.payment.exception;

import lombok.Getter;

@Getter
public class NotFoundPaymentException extends RuntimeException {
    private final PaymentErrorCode errorCode;

    public NotFoundPaymentException() {
        super(PaymentErrorCode.PAYMENT_NOT_FOUND.getMessage());
        this.errorCode = PaymentErrorCode.PAYMENT_NOT_FOUND;
    }

    public NotFoundPaymentException(Throwable cause) {
        super(PaymentErrorCode.PAYMENT_NOT_FOUND.getMessage(), cause);
        this.errorCode = PaymentErrorCode.PAYMENT_NOT_FOUND;
    }
}
