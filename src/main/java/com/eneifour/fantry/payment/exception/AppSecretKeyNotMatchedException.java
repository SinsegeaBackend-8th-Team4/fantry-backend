package com.eneifour.fantry.payment.exception;

public class AppSecretKeyNotMatchedException extends BootpayException {
    public AppSecretKeyNotMatchedException() {
        super(PaymentErrorCode.APP_SK_NOT_MATCHED);
    }

    public AppSecretKeyNotMatchedException(Throwable cause) {
        super(PaymentErrorCode.APP_SK_NOT_MATCHED, cause);
    }
}
