package com.eneifour.fantry.payment.exception;

public class BootpayTimeoutException extends BootpayException{
    public BootpayTimeoutException() {
        super(PaymentErrorCode.BOOTPAY_TIMEOUT);
    }

    public BootpayTimeoutException(Throwable cause) {
        super(PaymentErrorCode.BOOTPAY_TIMEOUT, cause);
    }
}
