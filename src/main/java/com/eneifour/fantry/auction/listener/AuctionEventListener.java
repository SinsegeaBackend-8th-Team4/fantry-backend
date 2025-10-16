package com.eneifour.fantry.auction.listener;

import com.eneifour.fantry.auction.domain.AuctionClosedEvent;
import com.eneifour.fantry.auction.dto.AuctionDetailResponse;
import com.eneifour.fantry.auction.repository.AuctionRepository;
import com.eneifour.fantry.bid.domain.Bid;
import com.eneifour.fantry.bid.repository.BidRepository;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.repository.MemberRepository;
import com.eneifour.fantry.notification.dto.Notification;
import com.eneifour.fantry.notification.service.SseConnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

/**
 * 경매 관련 이벤트를 처리하는 리스너
 * 트랜잭션 커밋 후 비동기로 알림을 전송합니다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AuctionEventListener {
    private final SseConnectionService sseConnectionService;
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final MemberRepository memberRepository;

    /**
     * 경매 마감 이벤트 처리
     * - 트랜잭션 커밋 후 실행 (TransactionPhase.AFTER_COMMIT)
     * - 비동기 처리 (@Async)
     * - 알림 전송 실패가 경매 마감 작업에 영향을 주지 않음
     */
    @Async
    @Transactional(readOnly = true, propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAuctionClosed(AuctionClosedEvent event) {
        try {
            log.info("Processing auction closed event for auctionId: {}", event.getAuctionId());

            // 0. 필요한 데이터 조회
            Optional<AuctionDetailResponse> auctionOptional = auctionRepository.findByAuctionId(event.getAuctionId());
            if (auctionOptional.isEmpty()) {
                log.warn("Auction {} not found, skipping notification", event.getAuctionId());
                return;
            }

            AuctionDetailResponse auction = auctionOptional.get();
            String itemName = auction.getItemName();

            // 1. 낙찰자 조회
            Optional<Bid> winningBidOptional = bidRepository.findTopByItemIdOrderByBidAmountDesc(event.getAuctionId());
            if (winningBidOptional.isEmpty()) {
                log.debug("Auction {} ended with no winner, skipping notification", event.getAuctionId());
                return;
            }

            // 2. 경매 종료 알림 (모든 구독자에게 브로드캐스트)
            Notification endNotification = Notification.createNotification(
                    Notification.NotificationType.AUCTION_END,
                    event.getAuctionId(),
                    "\"" + itemName + "\" 의 경매가 종료되었습니다."
            );
            sseConnectionService.broadcastToAuctionSubscribers(event.getAuctionId(), endNotification);
            Bid winningBid = winningBidOptional.get();
            int winnerId = winningBid.getBidderId();
            Optional<Member> winner = memberRepository.findById(winnerId);
            if(winner.isEmpty()) {
                return;
            }
            // 3. 낙찰 알림 (낙찰자에게만 개별 전송)
            Notification winnerNotification = Notification.createNotification(
                    Notification.NotificationType.BID_SUCCESSFUL,
                    event.getAuctionId(),
                    "\"" + itemName + "\" 의 경매에 낙찰되었습니다."
            );
            sseConnectionService.sendNotificationToUser(winner.get().getId(), winnerNotification);

            log.info("Successfully sent auction closed notifications for auctionId: {}", event.getAuctionId());

        } catch (Exception e) {
            log.error("Failed to send auction closed notification for auctionId: {}. Error: {}",
                    event.getAuctionId(), e.getMessage(), e);
            // 알림 실패는 경매 마감 작업에 영향을 주지 않음 (별도의 재시도 로직 필요시 구현)
        }
    }
}
