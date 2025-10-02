package com.eneifour.fantry.security.exception;

import com.eneifour.fantry.member.exception.MemberErrorCode;
import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
    private final AuthErrorCode authErrorCode;

    public AuthException(AuthErrorCode authErrorCode) {
        super(authErrorCode.getMessage());
        this.authErrorCode = authErrorCode;
    }

    public AuthException(AuthErrorCode authErrorCode, Throwable cause) {
        super(authErrorCode.getMessage(), cause);
        this.authErrorCode = authErrorCode;
    }
}
