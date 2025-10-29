package com.eneifour.fantry.payment.exception;

import com.eneifour.fantry.payment.domain.PaymentErrorCode;
import lombok.Getter;

@Getter
public class PaymentConfirmException extends PaymentException {
    private final PaymentErrorCode errorCode;

    public PaymentConfirmException() {
        super(PaymentErrorCode.PAYMENT_CONFIRM_FAILED);
        this.errorCode = PaymentErrorCode.PAYMENT_CONFIRM_FAILED;
    }

    public PaymentConfirmException(Throwable cause) {
        super(PaymentErrorCode.PAYMENT_CONFIRM_FAILED, cause);
        this.errorCode = PaymentErrorCode.PAYMENT_CONFIRM_FAILED;
    }
}
