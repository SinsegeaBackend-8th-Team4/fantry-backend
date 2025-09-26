package com.eneifour.fantry.security.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JwtRefreshTokenException extends RuntimeException {
    public JwtRefreshTokenException(String message) {
        super(message);
    }

    public JwtRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtRefreshTokenException(Throwable cause) {
        super(cause);
    }
}
