package com.eneifour.fantry.bid.controller;

import com.eneifour.fantry.bid.dto.BidRequest;
import com.eneifour.fantry.bid.service.BidService;
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
