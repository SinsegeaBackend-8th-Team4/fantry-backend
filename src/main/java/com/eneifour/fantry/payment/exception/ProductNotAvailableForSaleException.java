package com.eneifour.fantry.payment.exception;

public class ProductNotAvailableForSaleException extends RuntimeException{
    public ProductNotAvailableForSaleException(String message) {
        super(message);
    }
}
