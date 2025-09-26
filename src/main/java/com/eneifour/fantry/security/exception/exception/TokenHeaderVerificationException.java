package com.eneifour.fantry.security.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenHeaderVerificationException extends RuntimeException{
    public TokenHeaderVerificationException(String message){
        super(message);
    }

    public TokenHeaderVerificationException(String message, Throwable cause){
        super(message,cause);
    }

    public TokenHeaderVerificationException(Throwable cause){
        super(cause);
    }
}
