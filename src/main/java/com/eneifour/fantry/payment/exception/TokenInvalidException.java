package com.eneifour.fantry.payment.exception;

import com.eneifour.fantry.payment.domain.PaymentErrorCode;

public class TokenInvalidException extends BootpayException {
    public TokenInvalidException() {
        super(PaymentErrorCode.TOKEN_INVALID);
    }

    public TokenInvalidException(Throwable cause) {
        super(PaymentErrorCode.TOKEN_INVALID, cause);
    }
}
