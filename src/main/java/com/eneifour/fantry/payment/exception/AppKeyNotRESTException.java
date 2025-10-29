package com.eneifour.fantry.payment.exception;

import com.eneifour.fantry.payment.domain.PaymentErrorCode;

public class AppKeyNotRESTException extends BootpayException {
    public AppKeyNotRESTException() {
        super(PaymentErrorCode.APP_KEY_NOT_REST);
    }

    public AppKeyNotRESTException(Throwable cause) {
        super(PaymentErrorCode.APP_KEY_NOT_REST, cause);
    }
}
