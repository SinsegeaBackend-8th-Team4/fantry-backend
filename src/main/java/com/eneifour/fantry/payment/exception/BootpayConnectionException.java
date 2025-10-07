package com.eneifour.fantry.payment.exception;

import com.eneifour.fantry.payment.domain.PaymentErrorCode;

public class BootpayConnectionException extends BootpayException{
    public BootpayConnectionException() {
        super(PaymentErrorCode.BOOTPAY_CONNECTION_ERROR);
    }

    public BootpayConnectionException(Throwable cause) {
        super(PaymentErrorCode.BOOTPAY_CONNECTION_ERROR, cause);
    }
}
