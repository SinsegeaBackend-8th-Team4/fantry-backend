package com.eneifour.fantry.payment.exception;

import lombok.Getter;

@Getter
public class CreatePaymentFailedException extends RuntimeException {
    private final PaymentErrorCode errorCode;

    public CreatePaymentFailedException() {
        super(PaymentErrorCode.CREATE_PAYMENT_FAILED.getMessage());
        this.errorCode = PaymentErrorCode.CREATE_PAYMENT_FAILED;
    }

    public CreatePaymentFailedException(Throwable cause) {
        super(PaymentErrorCode.CREATE_PAYMENT_FAILED.getMessage(), cause);
        this.errorCode = PaymentErrorCode.CREATE_PAYMENT_FAILED;
    }
}
