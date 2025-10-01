package com.eneifour.fantry.payment.exception;

public class AppKeyNotFoundException extends BootpayException {
    public AppKeyNotFoundException() {
        super(PaymentErrorCode.APP_KEY_NOT_FOUND);
    }

    public AppKeyNotFoundException(Throwable cause) {
        super(PaymentErrorCode.APP_KEY_NOT_FOUND, cause);
    }
}
