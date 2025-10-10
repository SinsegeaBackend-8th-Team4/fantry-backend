package com.eneifour.fantry.payment.exception;

import com.eneifour.fantry.payment.domain.PaymentErrorCode;
import lombok.Getter;

@Getter
public class ProductNotAvailableForSaleException extends PaymentException{
    private final PaymentErrorCode errorCode;

    public ProductNotAvailableForSaleException() {
        super(PaymentErrorCode.PRODUCT_NOT_AVAILABLE);
        this.errorCode = PaymentErrorCode.PRODUCT_NOT_AVAILABLE;
    }

    public ProductNotAvailableForSaleException(Throwable cause) {
        super(PaymentErrorCode.PRODUCT_NOT_AVAILABLE, cause);
        this.errorCode = PaymentErrorCode.PRODUCT_NOT_AVAILABLE;
    }
}
