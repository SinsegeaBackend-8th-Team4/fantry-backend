package com.eneifour.fantry.payment.exception;

import lombok.Getter;

@Getter
public class PaymentAmountMismatchException extends RuntimeException {
    private final PaymentErrorCode errorCode;

    public PaymentAmountMismatchException() {
        super(PaymentErrorCode.PAYMENT_AMOUNT_MISMATCH.getMessage());
        this.errorCode = PaymentErrorCode.PAYMENT_AMOUNT_MISMATCH;
    }

    public PaymentAmountMismatchException(Throwable cause) {
        super(PaymentErrorCode.PAYMENT_AMOUNT_MISMATCH.getMessage(), cause);
        this.errorCode = PaymentErrorCode.PAYMENT_AMOUNT_MISMATCH;
    }
}
