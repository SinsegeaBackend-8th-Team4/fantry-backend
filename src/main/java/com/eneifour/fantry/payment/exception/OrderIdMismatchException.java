package com.eneifour.fantry.payment.exception;

import com.eneifour.fantry.payment.domain.PaymentErrorCode;

public class OrderIdMismatchException extends PaymentException {
    public OrderIdMismatchException() {
        super(PaymentErrorCode.ORDER_ID_MISMATCH);
    }
}
