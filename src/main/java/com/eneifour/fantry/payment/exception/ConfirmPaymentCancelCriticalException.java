package com.eneifour.fantry.payment.exception;

public class ConfirmPaymentCancelCriticalException extends BootpayException {
    public ConfirmPaymentCancelCriticalException() {
        super(PaymentErrorCode.RC_CANCEL_CRITICAL_ERROR);
    }

    public ConfirmPaymentCancelCriticalException(Throwable cause) {
        super(PaymentErrorCode.RC_CANCEL_CRITICAL_ERROR, cause);
    }
}
