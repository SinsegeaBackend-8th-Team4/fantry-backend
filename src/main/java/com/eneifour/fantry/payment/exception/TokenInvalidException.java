package com.eneifour.fantry.payment.exception;

public class TokenInvalidException extends BootpayException {
    public TokenInvalidException() {
        super(PaymentErrorCode.TOKEN_INVALID);
    }

    public TokenInvalidException(Throwable cause) {
        super(PaymentErrorCode.TOKEN_INVALID, cause);
    }
}
