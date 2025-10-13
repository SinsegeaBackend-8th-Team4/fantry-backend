package com.eneifour.fantry.bid.controller;

import com.eneifour.fantry.bid.domain.Bid;
import com.eneifour.fantry.bid.service.BidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bids")
@Slf4j
public class BidApiController {
    private final BidService bidService;

    /**
     * 전체 입찰 내역 조회
     */
    @GetMapping
    public ResponseEntity<List<Bid>> getAllBids() {
        log.info("Request to get all bid logs");
        List<Bid> bids = bidService.findAll();
        return ResponseEntity.ok(bids);
    }

    //특정 상품의 입찰내역 내림차순 조회
    @GetMapping("/auctions/{auctionId}")
    public ResponseEntity<List<Bid>> getBidsByAuctionId(@PathVariable int auctionId) {
        List bidList = bidService.findByItemId(auctionId);

        return ResponseEntity.ok(bidList);
    }

    /**
     * 특정 회원의 입찰 내역 조회
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Bid>> getBidsByMemberId(@PathVariable int memberId) {
        log.info("Request to get bids for memberId: {}", memberId);
        List<Bid> bids = bidService.findByBidderId(memberId);
        return ResponseEntity.ok(bids);
    }

    /**
     * 특정 회원 및 상품 기준 입찰 내역 조회
     */
    @GetMapping("/search")
    public ResponseEntity<List<Bid>> getBidsByMemberAndItem(
            @RequestParam int memberId,
            @RequestParam int itemId) {
        log.info("Request to get bids for memberId: {} and itemId: {}", memberId, itemId);
        List<Bid> bids = bidService.findByBidderIdAndItemId(memberId, itemId);
        return ResponseEntity.ok(bids);
    }

}
