package com.eneifour.fantry.payment.exception;

import lombok.Getter;

@Getter
public class CancellableAmountExceededException extends RuntimeException{
    private final PaymentErrorCode errorCode;

    public CancellableAmountExceededException() {
        super(PaymentErrorCode.CANCELLABLE_AMOUNT_EXCEEDED.getMessage());
        this.errorCode = PaymentErrorCode.CANCELLABLE_AMOUNT_EXCEEDED;
    }

    public CancellableAmountExceededException(Throwable cause) {
        super(PaymentErrorCode.CANCELLABLE_AMOUNT_EXCEEDED.getMessage(), cause);
        this.errorCode = PaymentErrorCode.CANCELLABLE_AMOUNT_EXCEEDED;
    }
}
