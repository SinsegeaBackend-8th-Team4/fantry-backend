package com.eneifour.fantry.payment.exception;

import com.eneifour.fantry.payment.domain.PaymentErrorCode;

public class PaymentAmountMismatchException extends PaymentException {
    public PaymentAmountMismatchException() {
        super(PaymentErrorCode.PAYMENT_AMOUNT_MISMATCH);
    }

    public PaymentAmountMismatchException(Throwable cause) {
        super(PaymentErrorCode.PAYMENT_AMOUNT_MISMATCH, cause);
    }
}
