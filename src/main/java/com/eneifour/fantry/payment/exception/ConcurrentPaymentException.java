package com.eneifour.fantry.payment.exception;

import com.eneifour.fantry.payment.domain.PaymentErrorCode;

public class ConcurrentPaymentException extends PaymentException {
    public ConcurrentPaymentException() {
        super(PaymentErrorCode.CONCURRENT_PAYMENT);
    }

    public ConcurrentPaymentException(Throwable cause) {
        super(PaymentErrorCode.CONCURRENT_PAYMENT, cause);
    }
}
