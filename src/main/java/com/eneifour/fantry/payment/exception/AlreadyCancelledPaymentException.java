package com.eneifour.fantry.payment.exception;

import com.eneifour.fantry.payment.domain.PaymentErrorCode;

/**
 * 이미 취소된 결제를 재취소하려고 할 때 발생하는 예외
 */
public class AlreadyCancelledPaymentException extends BootpayException {
    public AlreadyCancelledPaymentException() {
        super(PaymentErrorCode.RC_ALREADY_CANCELLED);
    }

    public AlreadyCancelledPaymentException(Throwable cause) {
        super(PaymentErrorCode.RC_ALREADY_CANCELLED, cause);
    }
}
