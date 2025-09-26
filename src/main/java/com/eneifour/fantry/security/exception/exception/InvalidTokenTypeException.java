package com.eneifour.fantry.security.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidTokenTypeException extends RuntimeException {
    public InvalidTokenTypeException(String message) {
        super(message);
    }

    public InvalidTokenTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTokenTypeException(Throwable cause) {
        super(cause);
    }
}
