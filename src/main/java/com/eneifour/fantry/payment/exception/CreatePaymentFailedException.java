package com.eneifour.fantry.payment.exception;

import com.eneifour.fantry.payment.domain.PaymentErrorCode;

public class CreatePaymentFailedException extends PaymentException {
    public CreatePaymentFailedException() {
        super(PaymentErrorCode.CREATE_PAYMENT_FAILED);
    }

    public CreatePaymentFailedException(Throwable cause) {
        super(PaymentErrorCode.CREATE_PAYMENT_FAILED, cause);
    }
}
