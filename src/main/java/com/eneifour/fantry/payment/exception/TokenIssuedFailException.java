package com.eneifour.fantry.payment.exception;

import com.eneifour.fantry.payment.domain.PaymentErrorCode;
import lombok.Getter;

@Getter
public class TokenIssuedFailException extends RuntimeException {
    private final PaymentErrorCode errorCode;

    public TokenIssuedFailException() {
        super(PaymentErrorCode.TOKEN_ISSUED_FAILED.getMessage());
        this.errorCode = PaymentErrorCode.TOKEN_ISSUED_FAILED;
    }

    public TokenIssuedFailException(Throwable cause) {
        super(PaymentErrorCode.TOKEN_ISSUED_FAILED.getMessage(), cause);
        this.errorCode = PaymentErrorCode.TOKEN_ISSUED_FAILED;
    }
}
