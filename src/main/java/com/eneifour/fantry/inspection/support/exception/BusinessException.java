package com.eneifour.fantry.inspection.support.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final BaseErrorCode errorCode;

    public BusinessException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
