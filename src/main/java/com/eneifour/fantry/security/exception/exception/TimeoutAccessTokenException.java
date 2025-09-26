package com.eneifour.fantry.security.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TimeoutAccessTokenException extends RuntimeException {
    public TimeoutAccessTokenException(String message) {
        super(message);
    }

    public TimeoutAccessTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeoutAccessTokenException(Throwable cause) {
        super(cause);
    }
}
