package com.eneifour.fantry.payment.exception;

public class CancellableAmountExceededException extends RuntimeException{
    public CancellableAmountExceededException(String message) {
        super(message);
    }
}
