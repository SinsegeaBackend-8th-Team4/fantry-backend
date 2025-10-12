package com.eneifour.fantry.auction.controller;

import com.eneifour.fantry.auction.dto.BidRequest;
import com.eneifour.fantry.auction.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BidSocketController {
    private final BidService bidService;

    @MessageMapping("/auctions/{auctionId}/bids")
    public void placeBid(@DestinationVariable int auctionId, BidRequest bidRequest){
        bidService.placeBid(auctionId, bidRequest);

    }


}
