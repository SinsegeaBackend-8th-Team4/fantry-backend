package com.eneifour.fantry.saleItem.exception;

public class BidException extends RuntimeException{
    public BidException(String msg){
        super(msg);
    }

    public BidException(String msg, Throwable e){
        super(msg,e);
    }

    public BidException(Throwable e){
        super(e);
    }
}
