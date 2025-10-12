package com.eneifour.fantry.auction.service;

import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.auction.domain.Bid;
import com.eneifour.fantry.auction.domain.SaleStatus;
import com.eneifour.fantry.auction.dto.BidRequest;
import com.eneifour.fantry.auction.exception.AuctionException;
import com.eneifour.fantry.auction.exception.BidException;
import com.eneifour.fantry.auction.exception.ErrorCode;
import com.eneifour.fantry.auction.exception.MemberException;
import com.eneifour.fantry.auction.repository.AuctionRepository;
import com.eneifour.fantry.auction.repository.BidRepository;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.repository.JpaMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BidDbFallbackHandler {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final JpaMemberRepository memberRepository;
    private final BidActionHelper bidActionHelper;

    private static final int MIN_BID_INCREMENT = 1000;

    /**
     * Redis 장애 시 호출되는 Fallback 메서드.
     * DB의 비관적 락(Pessimistic Lock)을 사용하여 동시성을 제어.
     * // @param auction 경매 정보
     * // @param bidDTO 입찰 정보 DTO
     */
    @Transactional
    public void placeBidWithDBLock(Auction auction, BidRequest bidRequest) {
        int bidAmount = bidRequest.getBidAmount();
        int auctionId = auction.getAuctionId();
        log.debug("Executing DB fallback logic with pessimistic lock for auctionId: {}", auctionId);

        // 1. 비관적 락을 걸어 최신 Auction 정보를 다시 조회.
        Auction lockedAuction = auctionRepository.findByIdForUpdate(auctionId)
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));

        // 2. 락 획득 직후, 최신 상태를 다시 한번 검증.
        if (lockedAuction.getSaleStatus() != SaleStatus.ACTIVE) {
            throw new AuctionException(ErrorCode.AUCTION_NOT_ACTIVE);
        }

        // 3. DB에서 현재 최고 입찰가 조회
        Optional<Bid> topBidOptional = bidRepository.findTopByItemIdOrderByBidAmountDesc(auctionId);

        // 4. DB 기반 입찰가 검증
        validateBidAmountWithDB(bidAmount, topBidOptional, lockedAuction.getStartPrice());

        // 5. 비상 상황에서는 큐를 사용하지 않고, 즉시 동기적으로 DB에 저장.
        Member bidder = memberRepository.findById(bidRequest.getMemberId())
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        Bid bidToLog = bidActionHelper.createBidLog(lockedAuction, bidder, bidAmount);

        bidRepository.save(bidToLog);
        log.info("Synchronously saved bid for auctionId {} during DB fallback.", auctionId);

        // 6. 브로드캐스팅
        bidActionHelper.broadcastNewBid(lockedAuction.getAuctionId(), bidder, bidAmount);
    }

    /**
     * DB를 기준으로 입찰 유효성을 검사. (Fallback 전용)
     */
    private void validateBidAmountWithDB(int bidAmount, Optional<Bid> topBidOptional, int startPrice) {
        if (topBidOptional.isPresent()) {
            int currentHighestBid = topBidOptional.get().getBidAmount();
            if (bidAmount < currentHighestBid + MIN_BID_INCREMENT) {
                throw new BidException(ErrorCode.BID_TOO_LOW_INCREMENT);
            }
        } else {
            if (bidAmount <= startPrice) {
                throw new BidException(ErrorCode.BID_TOO_LOW_START);
            }
        }
    }

}
