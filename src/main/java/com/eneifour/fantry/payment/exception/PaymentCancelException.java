package com.eneifour.fantry.payment.exception;

public class PaymentCancelException extends RuntimeException {
    public PaymentCancelException(Throwable cause) {
        super(cause);
    }

    public PaymentCancelException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentCancelException(String message) {
        super(message);
    }

    public PaymentCancelException() {
    }
}
