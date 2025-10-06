package com.eneifour.fantry.address.exception;

import lombok.Getter;

@Getter
public class AddressException extends RuntimeException{
    private AddressErrorCode addressErrorCode;

    public AddressException(AddressErrorCode errorCode) {
        super(errorCode.getMessage());
        this.addressErrorCode = errorCode;
    }

    public AddressException(AddressErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.addressErrorCode = errorCode;
    }
}
