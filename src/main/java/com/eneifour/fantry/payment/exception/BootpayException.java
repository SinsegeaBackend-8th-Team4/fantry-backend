package com.eneifour.fantry.payment.exception;

import com.eneifour.fantry.payment.domain.PaymentErrorCode;

/**
 * Bootpay API 관련 예외
 */
public class BootpayException extends PaymentException {
    public BootpayException(PaymentErrorCode errorCode) {
        super(errorCode);
    }

    public BootpayException(PaymentErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
