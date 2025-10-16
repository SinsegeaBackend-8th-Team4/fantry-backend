package com.eneifour.fantry.auction.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 경매 마감 이벤트
 * 경매가 정상적으로 종료되고 낙찰자가 결정된 후 발행됩니다.
 */
@Getter
@AllArgsConstructor
public class AuctionClosedEvent {
    /**
     * 경매 ID
     */
    private final Integer auctionId;
}