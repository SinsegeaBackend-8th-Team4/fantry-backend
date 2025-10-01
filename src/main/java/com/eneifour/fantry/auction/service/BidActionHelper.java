package com.eneifour.fantry.auction.service;

import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.auction.domain.Bid;
import com.eneifour.fantry.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

//브로드캐스팅 , In-memory Queue 에 저장할 Bid 객체 Set

@Component
@RequiredArgsConstructor
public class BidActionHelper {

    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 새 입찰 정보를 클라이언트에게 브로드캐스팅합니다.
     */
    public void broadcastNewBid(int auctionId, Member bidder, int newPrice) {
        simpMessagingTemplate.convertAndSend("/topic/auctions/" + auctionId, Map.of(
                "newPrice", newPrice,
                "memberId", bidder.getId()
        ));
    }

    /**
     * DB에 저장할 Bid 엔티티를 생성.
     */
    public Bid createBidLog(Auction auction, Member bidder, int bidAmount) {
        return Bid.builder()
                .bidAmount(bidAmount)
                .bidderId(bidder.getMemberId())
                .bidderName(bidder.getName())
                .itemId(auction.getAuctionId())
                .itemName(auction.getProductInspection().getItemName())
                .build();
    }
}
