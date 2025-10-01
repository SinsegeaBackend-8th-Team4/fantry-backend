package com.eneifour.fantry.payment.exception;

import lombok.Getter;

@Getter
public class ProductNotAvailableForSaleException extends RuntimeException{
    private final PaymentErrorCode errorCode;

    public ProductNotAvailableForSaleException() {
        super(PaymentErrorCode.PRODUCT_NOT_AVAILABLE.getMessage());
        this.errorCode = PaymentErrorCode.PRODUCT_NOT_AVAILABLE;
    }

    public ProductNotAvailableForSaleException(Throwable cause) {
        super(PaymentErrorCode.PRODUCT_NOT_AVAILABLE.getMessage(), cause);
        this.errorCode = PaymentErrorCode.PRODUCT_NOT_AVAILABLE;
    }
}
