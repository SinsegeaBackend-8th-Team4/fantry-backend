package com.eneifour.fantry.saleItem.controller;

import com.eneifour.fantry.saleItem.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class AuctionController {
    private final AuctionService auctionService;


    @GetMapping("/auctions/{auctionId}")
    public ResponseEntity<?> getAuctionById(@PathVariable("auctionId") int auctionId){
        auctionService.findOne(auctionId);

        return ResponseEntity.ok(Map.of("result","조회 성공"));
    }
}
