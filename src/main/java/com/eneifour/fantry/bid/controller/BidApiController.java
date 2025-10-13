package com.eneifour.fantry.bid.controller;

import com.eneifour.fantry.bid.domain.Bid;
import com.eneifour.fantry.bid.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bids")
public class BidApiController {
    private final BidService bidService;

    @GetMapping("/auctions/{auctionId}")
    public ResponseEntity<List<Bid>> getBidsByAuctionId(@PathVariable int auctionId) {
        List bidList = bidService.findByItemId(auctionId);

        return ResponseEntity.ok(bidList);
    }

}
