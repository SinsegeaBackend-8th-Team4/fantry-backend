package com.eneifour.fantry.payment.exception;

import lombok.Getter;

@Getter
public class ConcurrentPaymentException extends RuntimeException {
    private final PaymentErrorCode errorCode;

    public ConcurrentPaymentException() {
        super(PaymentErrorCode.CONCURRENT_PAYMENT.getMessage());
        this.errorCode = PaymentErrorCode.CONCURRENT_PAYMENT;
    }

    public ConcurrentPaymentException(Throwable cause) {
        super(PaymentErrorCode.CONCURRENT_PAYMENT.getMessage(), cause);
        this.errorCode = PaymentErrorCode.CONCURRENT_PAYMENT;
    }
}
