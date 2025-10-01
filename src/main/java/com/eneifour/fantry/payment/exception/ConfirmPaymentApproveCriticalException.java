package com.eneifour.fantry.payment.exception;

public class ConfirmPaymentApproveCriticalException extends BootpayException {
    public ConfirmPaymentApproveCriticalException() {
        super(PaymentErrorCode.RC_CONFIRM_CRITICAL_FAILED);
    }

    public ConfirmPaymentApproveCriticalException(Throwable cause) {
        super(PaymentErrorCode.RC_CONFIRM_CRITICAL_FAILED, cause);
    }
}
