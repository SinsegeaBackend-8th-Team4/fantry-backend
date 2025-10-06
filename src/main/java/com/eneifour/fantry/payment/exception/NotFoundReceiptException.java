package com.eneifour.fantry.payment.exception;

import com.eneifour.fantry.payment.domain.PaymentErrorCode;

public class NotFoundReceiptException extends BootpayException {
    public NotFoundReceiptException() {
        super(PaymentErrorCode.RC_NOT_FOUND);
    }

    public NotFoundReceiptException(Throwable cause) {
        super(PaymentErrorCode.RC_NOT_FOUND, cause);
    }
}
