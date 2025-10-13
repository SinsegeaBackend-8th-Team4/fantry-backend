package com.eneifour.fantry.auction.exception;

public class RedisException extends BusinessException{
    public RedisException(ErrorCode errorCode) {
        super(errorCode);
    }
}
