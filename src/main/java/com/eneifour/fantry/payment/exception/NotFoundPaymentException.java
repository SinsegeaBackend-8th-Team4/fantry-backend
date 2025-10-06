package com.eneifour.fantry.payment.exception;

import com.eneifour.fantry.payment.domain.PaymentErrorCode;

public class NotFoundPaymentException extends PaymentException {
    public NotFoundPaymentException() {
        super(PaymentErrorCode.PAYMENT_NOT_FOUND);
    }
}
