package com.eneifour.fantry.payment.exception;

import com.eneifour.fantry.payment.domain.PaymentErrorCode;
import lombok.Getter;

@Getter
public class CancellableAmountExceededException extends PaymentException{
    private final PaymentErrorCode errorCode;

    public CancellableAmountExceededException() {
        super(PaymentErrorCode.CANCELLABLE_AMOUNT_EXCEEDED);
        this.errorCode = PaymentErrorCode.CANCELLABLE_AMOUNT_EXCEEDED;
    }

    public CancellableAmountExceededException(Throwable cause) {
        super(PaymentErrorCode.CANCELLABLE_AMOUNT_EXCEEDED, cause);
        this.errorCode = PaymentErrorCode.CANCELLABLE_AMOUNT_EXCEEDED;
    }
}
