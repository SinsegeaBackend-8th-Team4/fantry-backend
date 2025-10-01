package com.eneifour.fantry.auction.controller;

import com.eneifour.fantry.auction.dto.BidDTO;
import com.eneifour.fantry.auction.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BidController {
    private final BidService bidService;

    @MessageMapping("/auctions/{auctionId}/bids")
    public void placeBid(@DestinationVariable int auctionId, BidDTO bidDTO){
        bidService.placeBid(auctionId,bidDTO);

    }


}
