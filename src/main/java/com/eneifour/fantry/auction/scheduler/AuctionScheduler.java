package com.eneifour.fantry.auction.scheduler;

import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.auction.service.AuctionService;
import com.eneifour.fantry.bid.scheduler.BidScheduler;
import com.eneifour.fantry.auction.domain.SaleStatus;
import com.eneifour.fantry.auction.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuctionScheduler {

    private final AuctionRepository auctionRepository;
    private final AuctionService auctionService;
    private final BidScheduler bidScheduler;

    /**
     * 1분마다 실행하여 시작 시간이 지난 '준비중'인 경매를 찾아 '활성' 상태로 변경합니다.
     */
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void activateScheduledAuctions() {
        log.debug("Checking for auctions to activate...");

        Pageable pageable = PageRequest.of(0, 100); // 한 번에 100개씩 처리
        Page<Auction> scheduledAuctionsPage;

        // 1. '준비중(PREPARING)' 상태이면서, 시작 시간(startTime)이 지금보다 이전인 모든 경매를 조회
        do {
            scheduledAuctionsPage = auctionRepository.findBySaleStatusAndStartTimeBefore(
                    SaleStatus.PREPARING,
                    LocalDateTime.now(),
                    pageable
            );

            if (scheduledAuctionsPage.isEmpty()) {
                log.debug("No auctions to activate in this batch.");
                break;
            }

            log.info("Found {} auctions to activate.", scheduledAuctionsPage.getNumberOfElements());

            // 2. 각 경매를 개별 트랜잭션으로 처리하여, 하나가 실패해도 다른 경매에 영향을 주지 않도록 함
            for (Auction auction : scheduledAuctionsPage.getContent()) {
                try {
                    // 서비스 레이어의 activateAuction 메소드를 호출하여 상태를 변경
                    auctionService.activateAuction(auction.getAuctionId());
                    log.info("Auction ID: {} has been activated.", auction.getAuctionId());
                } catch (Exception e) {
                    // 개별 경매 처리 중 발생한 예외는 로깅만 하고 계속 진행
                    log.error("Failed to activate auction for auctionId: {}. Error: {}",
                            auction.getAuctionId(), e.getMessage(), e);
                }
            }
            pageable = scheduledAuctionsPage.nextPageable();
        } while (scheduledAuctionsPage.hasNext());
    }


    /**
     * 1분마다 실행하여 마감 시간이 지난 경매를 찾아 처리합니다.
     */
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void closeEndedAuctions() {
        log.debug("Checking for ended auctions...");

        // 낙찰자를 결정하기 전에, In-Memory Queue에 남아있는 모든 입찰 기록을
        // DB에 즉시 동기화하여 데이터 정합성을 보장합니다.
        bidScheduler.flushBidLogToDB();

        Pageable pageable = PageRequest.of(0, 100); // 한 번에 100건씩 처리
        Page<Auction> endedAuctionsPage;

        // 1. 현재 진행 중(ACTIVE)이면서, 마감 시간(endTime)이 지금보다 이전인 모든 경매를 조회
        do {
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
                    auctionService.processAuctionClosure(auction);
                } catch (Exception e) {
                    // 개별 경매 처리 중 발생한 예외는 로깅만 하고 계속 진행
                    log.error("Failed to process auction closure for auctionId: {}. Error: {}",
                            auction.getAuctionId(), e.getMessage(), e);
                }
            }
            pageable = endedAuctionsPage.nextPageable();
        } while (endedAuctionsPage.hasNext());
    }
}
