package com.eneifour.fantry.auction.controller;

import com.eneifour.fantry.auction.dto.AuctionDetailResponse;
import com.eneifour.fantry.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuctionController {
    private final AuctionService auctionService;


    @GetMapping("/auctions/{auctionId}")
    public ResponseEntity<?> getAuctionById(@PathVariable("auctionId") int auctionId){
        AuctionDetailResponse auctionDetail = auctionService.findOne(auctionId);

        return ResponseEntity.ok(auctionDetail);
    }
}
