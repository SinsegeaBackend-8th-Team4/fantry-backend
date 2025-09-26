package com.eneifour.fantry.payment.exception;

public class TokenIssuedFailException extends RuntimeException {
    public TokenIssuedFailException(String message) {
        super(message);
    }

    public TokenIssuedFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenIssuedFailException(Throwable cause) {
        super(cause);
    }

    public TokenIssuedFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
