package com.eneifour.fantry.auction.domain;

public enum SaleStatus {
    PREPARING, //판매 준비중
    ACTIVE, //판매 중
    SOLD, //판매 완료 (낙찰 or 즉시구매)
    NOT_SOLD, //유찰 or 판매 안됨
    CANCELED, //판매 취소
    REPREPARING, //재준비중
    REACTIVE //재판매중
}
