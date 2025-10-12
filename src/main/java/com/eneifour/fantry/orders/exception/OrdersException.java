package com.eneifour.fantry.orders.exception;

import com.eneifour.fantry.auction.exception.BusinessException;
import com.eneifour.fantry.auction.exception.ErrorCode;

public class OrdersException extends BusinessException {

    public OrdersException(ErrorCode errorCode) {
        super(errorCode);
    }
}
