package com.eneifour.fantry.saleItem.exception;

public class AuctionException extends RuntimeException{
    public AuctionException(String msg){
        super(msg);
    }

    public AuctionException(String msg,Throwable e){
        super(msg,e);
    }

    public AuctionException(Throwable e){
        super(e);
    }
}
