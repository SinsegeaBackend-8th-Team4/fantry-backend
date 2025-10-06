package com.eneifour.fantry.account.exception;

import lombok.Getter;

@Getter
public class AccountException extends RuntimeException{
    private AccountErrorCode accountErrorCode;

    public AccountException(AccountErrorCode errorCode) {
        super(errorCode.getMessage());
        this.accountErrorCode = errorCode;
    }

    public AccountException(AccountErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.accountErrorCode = errorCode;
    }
}
