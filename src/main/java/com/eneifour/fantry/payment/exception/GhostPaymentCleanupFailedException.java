package com.eneifour.fantry.payment.exception;

import com.eneifour.fantry.payment.domain.PaymentErrorCode;
import lombok.Getter;

@Getter
public class GhostPaymentCleanupFailedException extends PaymentException {
    private final PaymentErrorCode errorCode;

    public GhostPaymentCleanupFailedException() {
        super(PaymentErrorCode.GHOST_PAYMENT_CLEANUP_FAILED);
        this.errorCode = PaymentErrorCode.GHOST_PAYMENT_CLEANUP_FAILED;
    }

    public GhostPaymentCleanupFailedException(Throwable cause) {
        super(PaymentErrorCode.GHOST_PAYMENT_CLEANUP_FAILED, cause);
        this.errorCode = PaymentErrorCode.GHOST_PAYMENT_CLEANUP_FAILED;
    }
}
