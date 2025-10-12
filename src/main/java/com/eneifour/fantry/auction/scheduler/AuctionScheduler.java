package com.eneifour.fantry.auction.scheduler;

import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.bid.domain.Bid;
import com.eneifour.fantry.bid.scheduler.BidScheduler;
import com.eneifour.fantry.orders.domain.Orders;
import com.eneifour.fantry.auction.domain.SaleStatus;
import com.eneifour.fantry.auction.exception.ErrorCode;
import com.eneifour.fantry.auction.exception.MemberException;
import com.eneifour.fantry.auction.repository.AuctionRepository;
import com.eneifour.fantry.bid.repository.BidRepository;
import com.eneifour.fantry.orders.repository.OrdersRepository;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuctionScheduler {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final BlockingQueue<Bid> bidLogQueue;
    private final OrdersRepository ordersRepository;
    private final MemberRepository memberRepository;
    private final BidScheduler bidScheduler;


    /**
     * 1분마다 실행하여 마감 시간이 지난 경매를 찾아 처리합니다.
     */
    @Scheduled(fixedRate = 60000) // 60000ms = 1분
    public void closeEndedAuctions() {
        log.debug("Checking for ended auctions...");

        // 낙찰자를 결정하기 전에, In-Memory Queue에 남아있는 모든 입찰 기록을
        // DB에 즉시 동기화하여 데이터 정합성을 보장합니다.
        bidScheduler.flushBidLogToDB();

        // 페이징 처리를 위한 Pageable 객체 생성 (한 번에 100건씩 처리)
        Pageable pageable = PageRequest.of(0, 100);
        Page<Auction> endedAuctionsPage;

        // 1. 현재 진행 중(ACTIVE)이면서, 마감 시간(endTime)이 지금보다 이전인 모든 경매를 조회
        do {
            // [수정] 페이징을 사용하여 대용량 데이터를 안전하게 조회합니다.
            endedAuctionsPage = auctionRepository.findBySaleStatusAndEndTimeBefore(
                    SaleStatus.ACTIVE,
                    LocalDateTime.now(),
                    pageable
            );

            if (endedAuctionsPage.isEmpty()) {
                log.debug("No auctions have ended in this batch.");
                break;
            }

            log.info("Found {} auctions to close.", endedAuctionsPage.getNumberOfElements());

            // 2. 각 경매를 개별 트랜잭션으로 처리하여, 하나가 실패해도 다른 경매에 영향을 주지 않도록 함
            for (Auction auction : endedAuctionsPage.getContent()) {
                try {
                    processAuctionClosure(auction);
                } catch (Exception e) {
                    // 개별 경매 처리 중 발생한 예외는 로깅만 하고 계속 진행
                    log.error("Failed to process auction closure for auctionId: {}. Error: {}",
                            auction.getAuctionId(), e.getMessage(), e);
                }
            }
            pageable = endedAuctionsPage.nextPageable();
        } while (endedAuctionsPage.hasNext());
    }
    /**
     * 단일 경매에 대한 마감 처리를 수행합니다.
     * 이 메서드는 하나의 원자적 단위로 동작해야 하므로 @Transactional을 적용.
     * //@param auction 마감 처리할 경매 객체
     */
    @Transactional
    public void processAuctionClosure(Auction auction) {
        log.info("Processing closure for auctionId: {}", auction.getAuctionId());

        // 1. 해당 경매의 최고 입찰(낙찰자)을 DB에서 조회
        Optional<Bid> winningBidOptional = bidRepository.findTopByItemIdOrderByBidAmountDesc(auction.getAuctionId());

        if (winningBidOptional.isPresent()) {
            // --- Case 1: 낙찰자가 있는 경우 ---
            Bid winningBid = winningBidOptional.get();
            int finalPrice = winningBid.getBidAmount();
            int winnerId = winningBid.getBidderId();

            // 1-1. 낙찰자 정보를 조회
            Member winner = memberRepository.findById(winnerId)
                    .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

            // 1-2. 경매 상태를 '완료'로 변경하고 최종 낙찰가 기록
            auction.closeAsSold(finalPrice);

            // 1-3. 새로운 주문(Orders) 생성
            Orders newOrder = Orders.builder()
                    .auction(auction)
                    .member(winner)
                    .price(finalPrice)
                    .build();

            // shippingAddress 등은 이후 사용자가 입력하도록 비워둠

            ordersRepository.save(newOrder);

            log.info("AuctionId {} has been successfully closed. Winner: {}, Final Price: {}",
                    auction.getAuctionId(), winner.getName(), finalPrice);

            // TODO: 낙찰자에게 알림을 보내는 로직을 추가할 수 있음 (e.g., WebSocket, SMS, Email)

        } else {
            // --- Case 2: 입찰자가 아무도 없어 유찰된 경우 ---
            log.info("AuctionId {} has ended with no bids. Status changed to CANCELLED.", auction.getAuctionId());
            auction.closeAsNotSold();
            // TODO: 판매자에게 유찰 알림을 보내는 로직을 추가할 수 있음
        }
    }




}
