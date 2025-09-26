package com.eneifour.fantry.payment.exception;

public class NotFoundReceiptException extends RuntimeException {
    public NotFoundReceiptException(Throwable cause) {
        super(cause);
    }

    public NotFoundReceiptException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundReceiptException(String message) {
        super(message);
    }

    public NotFoundReceiptException() {
    }
}
