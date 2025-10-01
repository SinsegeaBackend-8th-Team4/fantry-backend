package com.eneifour.fantry.payment.exception;

public class ConfirmPaymentCancelFailedException extends BootpayException {
    public ConfirmPaymentCancelFailedException() {
        super(PaymentErrorCode.RC_CANCEL_SERVER_ERROR);
    }

    public ConfirmPaymentCancelFailedException(Throwable cause) {
        super(PaymentErrorCode.RC_CANCEL_SERVER_ERROR, cause);
    }
}
