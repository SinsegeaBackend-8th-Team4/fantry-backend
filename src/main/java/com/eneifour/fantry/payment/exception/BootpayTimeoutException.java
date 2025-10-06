package com.eneifour.fantry.payment.exception;

import com.eneifour.fantry.payment.domain.PaymentErrorCode;

public class BootpayTimeoutException extends BootpayException{
    public BootpayTimeoutException() {
        super(PaymentErrorCode.BOOTPAY_TIMEOUT);
    }

    public BootpayTimeoutException(Throwable cause) {
        super(PaymentErrorCode.BOOTPAY_TIMEOUT, cause);
    }
}
