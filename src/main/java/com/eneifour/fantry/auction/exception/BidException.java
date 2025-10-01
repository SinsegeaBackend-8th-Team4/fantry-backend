package com.eneifour.fantry.auction.exception;

public class BidException extends BusinessException {
    public BidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
