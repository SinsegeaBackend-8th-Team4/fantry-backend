package com.eneifour.fantry.payment.exception;

import com.eneifour.fantry.payment.domain.PaymentErrorCode;
import lombok.Getter;

@Getter
public class PaymentCancelException extends RuntimeException {
    private final PaymentErrorCode errorCode;

    public PaymentCancelException() {
        super(PaymentErrorCode.PAYMENT_CANCEL_FAILED.getMessage());
        this.errorCode = PaymentErrorCode.PAYMENT_CANCEL_FAILED;
    }

    public PaymentCancelException(Throwable cause) {
        super(PaymentErrorCode.PAYMENT_CANCEL_FAILED.getMessage(), cause);
        this.errorCode = PaymentErrorCode.PAYMENT_CANCEL_FAILED;
    }
}
