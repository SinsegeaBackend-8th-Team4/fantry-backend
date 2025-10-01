package com.eneifour.fantry.payment.exception;

public class CreatePaymentFailedException extends RuntimeException {
    public CreatePaymentFailedException(Throwable cause) {
        super(cause);
    }

    public CreatePaymentFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreatePaymentFailedException(String message) {
        super(message);
    }

    public CreatePaymentFailedException() {
    }
}
