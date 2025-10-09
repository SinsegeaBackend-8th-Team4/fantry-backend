package com.eneifour.fantry.auction.controller;

import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.auction.domain.SaleStatus;
import com.eneifour.fantry.auction.domain.SaleType;
import com.eneifour.fantry.auction.dto.AuctionDetailResponse;
import com.eneifour.fantry.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auctions")
public class AuctionController {
    private final AuctionService auctionService;


    // 상품 단건 조회
    @GetMapping("/{auctionId}")
    public ResponseEntity<?> getAuctionById(@PathVariable("auctionId") int auctionId){
        log.warn("/api/auctions/"+auctionId);
        AuctionDetailResponse auctionDetail = auctionService.findOne(auctionId);

        return ResponseEntity.ok(auctionDetail);
    }

    // 전체 상품 조회
    @GetMapping
    public ResponseEntity<List<Auction>> getAuctions(
            @RequestParam(value = "saleType", required = false) SaleType saleType,
            @RequestParam(value = "saleStatus", required = false) SaleStatus saleStatus) {

        List<Auction> auctions = auctionService.findAll();
        return ResponseEntity.ok(auctions);
    }

    //member_id 에 따른 auction 상품 조회
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Auction>> getAuctionsByMemberId(@PathVariable("memberId") int memberId) {
        List<Auction> auctions = auctionService.findBymemberId(memberId);
        return ResponseEntity.ok(auctions);
    }

}
