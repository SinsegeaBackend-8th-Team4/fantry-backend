package com.eneifour.fantry.payment.exception;

public class GhostPaymentCleanupFailedException extends RuntimeException {
    public GhostPaymentCleanupFailedException() {
    }

    public GhostPaymentCleanupFailedException(String message) {
        super(message);
    }

    public GhostPaymentCleanupFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public GhostPaymentCleanupFailedException(Throwable cause) {
        super(cause);
    }
}
