package com.eneifour.fantry.auction.exception;

public class AuctionException extends BusinessException {

    public AuctionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
