package com.eneifour.fantry.auction.scheduler;

import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.auction.domain.Bid;
import com.eneifour.fantry.auction.domain.Orders;
import com.eneifour.fantry.auction.domain.SaleStatus;
import com.eneifour.fantry.auction.exception.ErrorCode;
import com.eneifour.fantry.auction.exception.MemberException;
import com.eneifour.fantry.auction.repository.AuctionRepository;
import com.eneifour.fantry.auction.repository.BidRepository;
import com.eneifour.fantry.auction.repository.OrdersRepository;
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
import java.util.ArrayList;
import java.util.List;
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

    //입찰 시도시 Bid -> In-Memory Queue 저장 스케쥴러 로직
    // fixedDelay: 이전 작업이 끝난 시점으로부터 2000ms(2초) 후에 다시 실행
    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void flushBidLogToDB() {
        // 1. 큐가 비어있으면 아무 작업도 하지 않고 즉시 종료 (불필요한 DB 접근 방지)
        if (bidLogQueue.isEmpty()) {
            return;
        }

        // 2. 큐에 현재 쌓여있는 모든 데이터를 리스트로 한번에 옮겨 담기.
        // drainTo는 원자적으로 동작하여 안전함.
        List<Bid> bidsToSave = new ArrayList<>();
        bidLogQueue.drainTo(bidsToSave);

        // 3. 옮겨 담은 데이터가 있을 경우에만 DB에 저장
        if (!bidsToSave.isEmpty()) {
            try {
                log.info("Attempting to save {} bid logs to DB in a batch.", bidsToSave.size());

                // 4. JPA의 saveAll을 사용하여 배치 INSERT 실행
                // application.properties의 batch_size 설정 필수.
                bidRepository.saveAll(bidsToSave);

                log.info("Successfully saved {} bid logs.", bidsToSave.size());
            } catch (Exception e) {
                log.error("Failed to save bid logs to DB. Batch size: {}. Error: {}", bidsToSave.size(), e.getMessage());
                // [고려사항] 실패 시 이 데이터를 어떻게 처리할지 정책결정
                // (예: 다시 큐에 넣기, 파일로 로깅하기 등)
            }
        }
    }

    /**
     * 1분마다 실행하여 마감 시간이 지난 경매를 찾아 처리합니다.
     */
    @Scheduled(fixedRate = 60000) // 60000ms = 1분
    public void closeEndedAuctions() {
        log.debug("Checking for ended auctions...");

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
                return;
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
            auction.closeAsSold(winningBid.getBidAmount());

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
