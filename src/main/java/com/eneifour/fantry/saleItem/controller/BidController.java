package com.eneifour.fantry.saleItem.controller;

import com.eneifour.fantry.saleItem.dto.BidDTO;
import com.eneifour.fantry.saleItem.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BidController {
    private final BidService bidService;

    @MessageMapping("/auctions/{auctionId}/bids")
    public void placeBid(@DestinationVariable int auctionId, BidDTO bidDTO){
        bidService.placeBid(auctionId,bidDTO);

    }


}
