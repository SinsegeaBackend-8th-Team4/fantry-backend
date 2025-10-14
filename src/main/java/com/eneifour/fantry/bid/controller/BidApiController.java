package com.eneifour.fantry.bid.controller;

import com.eneifour.fantry.bid.domain.Bid;
import com.eneifour.fantry.bid.service.BidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 입찰 관련 API를 제공하는 컨트롤러입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bids")
@Slf4j
public class BidApiController {
    private final BidService bidService;

    /**
     * 모든 입찰 내역을 조회합니다.
     *
     * @return 전체 입찰 내역 목록.
     */
    @GetMapping
    public ResponseEntity<List<Bid>> getAllBids() {
        log.info("Request to get all bid logs");
        List<Bid> bids = bidService.findAll();
        return ResponseEntity.ok(bids);
    }

    /**
     * 특정 상품에 대한 모든 입찰 내역을 가격이 높은 순으로 조회합니다.
     *
     * @param auctionId 조회할 상품의 ID.
     * @return 해당 상품의 입찰 내역 목록.
     */
    @GetMapping("/auctions/{auctionId}")
    public ResponseEntity<List<Bid>> getBidsByAuctionId(@PathVariable int auctionId) {
        List bidList = bidService.findByItemId(auctionId);

        return ResponseEntity.ok(bidList);
    }

    /**
     * 특정 회원의 모든 입찰 내역을 조회합니다.
     *
     * @param bidderId 조회할 회원의 ID.
     * @return 해당 회원의 입찰 내역 목록.
     */
    @GetMapping("/member/{bidderId}")
    public ResponseEntity<List<Bid>> getBidsByBidderId(@PathVariable int bidderId) {
        log.info("Request to get bids for bidderId: {}", bidderId);
        List<Bid> bids = bidService.findByBidderId(bidderId);
        return ResponseEntity.ok(bids);
    }

    /**
     * 특정 회원이 특정 상품에 입찰한 내역을 조회합니다.
     *
     * @param bidderId 조회할 회원의 ID.
     * @param itemId   조회할 상품의 ID.
     * @return 해당 조건에 맞는 입찰 내역 목록.
     */
    @GetMapping("/search")
    public ResponseEntity<List<Bid>> getBidsByBidderAndItem(
            @RequestParam int bidderId,
            @RequestParam int itemId) {
        log.info("Request to get bids for bidderId: {} and itemId: {}", bidderId, itemId);
        List<Bid> bids = bidService.findByBidderIdAndItemId(bidderId, itemId);
        return ResponseEntity.ok(bids);
    }

}
