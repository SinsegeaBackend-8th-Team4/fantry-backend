package com.eneifour.fantry.payment.exception;

public class ConfirmPaymentApproveFailedException extends BootpayException {
    public ConfirmPaymentApproveFailedException() {
        super(PaymentErrorCode.RC_CONFIRM_FAILED);
    }

    public ConfirmPaymentApproveFailedException(Throwable cause) {
        super(PaymentErrorCode.RC_CONFIRM_FAILED, cause);
    }
}
