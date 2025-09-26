package com.eneifour.fantry.security.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class LogoutException extends RuntimeException{
    public LogoutException(String message){
        super(message);
    }

    public LogoutException(String message, Throwable cause){
        super(message,cause);
    }

    public LogoutException(Throwable cause){
        super(cause);
    }
}
