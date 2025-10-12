package com.eneifour.fantry.bid.exception;

import com.eneifour.fantry.auction.exception.BusinessException;
import com.eneifour.fantry.auction.exception.ErrorCode;

public class BidException extends BusinessException {
    public BidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
