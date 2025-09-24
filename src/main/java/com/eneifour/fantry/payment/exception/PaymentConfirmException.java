package com.eneifour.fantry.payment.exception;

public class PaymentConfirmException extends RuntimeException {
    public PaymentConfirmException() {
    }

    public PaymentConfirmException(String message) {
        super(message);
    }

    public PaymentConfirmException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentConfirmException(Throwable cause) {
        super(cause);
    }
}
