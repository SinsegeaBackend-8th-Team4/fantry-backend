package com.eneifour.fantry.payment.exception;

public class NotFoundPaymentException extends RuntimeException {
    public NotFoundPaymentException(Throwable cause) {
        super(cause);
    }

    public NotFoundPaymentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundPaymentException(String message) {
        super(message);
    }

    public NotFoundPaymentException() {
    }
}
