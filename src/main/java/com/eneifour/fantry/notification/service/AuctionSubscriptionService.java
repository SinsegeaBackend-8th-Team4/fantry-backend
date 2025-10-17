package com.eneifour.fantry.notification.service;

import com.eneifour.fantry.notification.domain.UserAuctionSubscription;
import com.eneifour.fantry.notification.exception.NotificationErrorCode;
import com.eneifour.fantry.notification.exception.NotificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionSubscriptionService {

    private final Map<String, Map<Integer, UserAuctionSubscription>> userSubscriptions = new ConcurrentHashMap<>();
    private final Map<Integer, Set<String>> auctionSubscribers = new ConcurrentHashMap<>();

    public UserAuctionSubscription subscribe(String connectionId, Integer auctionId) {
        long startTime = System.currentTimeMillis();
        log.info("[AUCTION-SUBSCRIPTION] 구독 시작 - connectionId={}, auctionId={}", connectionId, auctionId);

        try {
            // 기존 구독이 있는지 확인
            UserAuctionSubscription existingSubscription = getSubscription(connectionId, auctionId);
            if (existingSubscription != null && existingSubscription.isActiveSubscription()) {
                log.info("[AUCTION-SUBSCRIPTION] 활성 구독 존재 - connectionId={}, auctionId={}, action=UPDATE_ACTIVITY",
                        connectionId, auctionId);
                existingSubscription.updateLastActivity();
                return existingSubscription;
            }

            log.debug("[AUCTION-SUBSCRIPTION] 새 구독 생성 중 - connectionId={}, auctionId={}", connectionId, auctionId);

            // 새로운 구독 생성
            UserAuctionSubscription subscription = UserAuctionSubscription.createSubscription(connectionId, auctionId);

            // 사용자별 구독 맵에 추가
            userSubscriptions.computeIfAbsent(connectionId, k -> new ConcurrentHashMap<>())
                    .put(auctionId, subscription);

            // 경매별 구독자 캐시에 추가
            auctionSubscribers.computeIfAbsent(auctionId, k -> ConcurrentHashMap.newKeySet())
                    .add(connectionId);

            long duration = System.currentTimeMillis() - startTime;
            log.info("[AUCTION-SUBSCRIPTION] 구독 완료 - connectionId={}, auctionId={}, durationMs={}, totalSubscribersForAuction={}",
                    connectionId, auctionId, duration, auctionSubscribers.get(auctionId).size());
            return subscription;

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("[AUCTION-SUBSCRIPTION] 구독 실패 - connectionId={}, auctionId={}, durationMs={}",
                    connectionId, auctionId, duration, e);
            throw new NotificationException(NotificationErrorCode.SUBSCRIPTION_FAILED, e);
        }
    }

    public Set<String> getAuctionSubscribers(Integer auctionId) {
        log.debug("[AUCTION-SUBSCRIPTION] 구독자 조회 시작 - auctionId={}", auctionId);

        Set<String> subscribers = auctionSubscribers.get(auctionId);
        if (subscribers == null) {
            log.debug("[AUCTION-SUBSCRIPTION] 구독자 없음 - auctionId={}", auctionId);
            return Collections.emptySet();
        }

        // 활성 구독자만 필터링
        Set<String> activeSubscribers = subscribers.stream()
                .filter(userId -> {
                    UserAuctionSubscription subscription = getSubscription(userId, auctionId);
                    boolean isActive = subscription != null && subscription.isActiveSubscription();
                    if (!isActive) {
                        log.trace("[AUCTION-SUBSCRIPTION] 비활성 구독자 필터링 - connectionId={}, auctionId={}",
                                userId, auctionId);
                    }
                    return isActive;
                })
                .collect(Collectors.toSet());

        log.debug("[AUCTION-SUBSCRIPTION] 구독자 조회 완료 - auctionId={}, totalSubscribers={}, activeSubscribers={}",
                auctionId, subscribers.size(), activeSubscribers.size());
        return activeSubscribers;
    }

    public int unsubscribeAll(String connectionId) {
        long startTime = System.currentTimeMillis();
        log.info("[AUCTION-SUBSCRIPTION] 전체 구독 해제 시작 - connectionId={}", connectionId);

        try {
            Map<Integer, UserAuctionSubscription> userSubs = userSubscriptions.get(connectionId);
            if (userSubs == null || userSubs.isEmpty()) {
                log.debug("[AUCTION-SUBSCRIPTION] 구독 정보 없음 - connectionId={}", connectionId);
                return 0;
            }

            int unsubscribedCount = 0;
            int totalSubscriptions = userSubs.size();

            log.debug("[AUCTION-SUBSCRIPTION] 구독 해제 진행 중 - connectionId={}, totalSubscriptions={}",
                    connectionId, totalSubscriptions);

            // 모든 구독 비활성화
            for (UserAuctionSubscription subscription : userSubs.values()) {
                if (subscription.isActiveSubscription()) {
                    Integer auctionId = subscription.getAuctionId();
                    subscription.deactivate();

                    // 경매별 구독자 캐시에서 제거
                    Set<String> subscribers = auctionSubscribers.get(auctionId);
                    if (subscribers != null) {
                        subscribers.remove(connectionId);
                        if (subscribers.isEmpty()) {
                            auctionSubscribers.remove(auctionId);
                            log.debug("[AUCTION-SUBSCRIPTION] 경매 구독자 캐시 제거 - auctionId={}, reason=NO_SUBSCRIBERS",
                                    auctionId);
                        }
                    }

                    unsubscribedCount++;
                    log.trace("[AUCTION-SUBSCRIPTION] 개별 구독 해제 - connectionId={}, auctionId={}",
                            connectionId, auctionId);
                }
            }

            long duration = System.currentTimeMillis() - startTime;
            log.info("[AUCTION-SUBSCRIPTION] 전체 구독 해제 완료 - connectionId={}, totalSubscriptions={}, unsubscribedCount={}, durationMs={}",
                    connectionId, totalSubscriptions, unsubscribedCount, duration);
            return unsubscribedCount;

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("[AUCTION-SUBSCRIPTION] 전체 구독 해제 실패 - connectionId={}, durationMs={}",
                    connectionId, duration, e);
            throw new NotificationException(NotificationErrorCode.UNSUBSCRIPTION_FAIL, e);
        }
    }

    public boolean unsubscribe(String userId, Integer auctionId) {
        long startTime = System.currentTimeMillis();
        log.info("[AUCTION-SUBSCRIPTION] 구독 해제 시작 - connectionId={}, auctionId={}", userId, auctionId);

        try {
            Map<Integer, UserAuctionSubscription> userSubs = userSubscriptions.get(userId);
            if (userSubs == null) {
                log.debug("[AUCTION-SUBSCRIPTION] 구독 정보 없음 - connectionId={}", userId);
                return false;
            }

            UserAuctionSubscription subscription = userSubs.get(auctionId);
            if (subscription == null) {
                log.debug("[AUCTION-SUBSCRIPTION] 해당 경매 구독 없음 - connectionId={}, auctionId={}", userId, auctionId);
                return false;
            }

            // 구독 비활성화
            subscription.deactivate();
            log.debug("[AUCTION-SUBSCRIPTION] 구독 비활성화 완료 - connectionId={}, auctionId={}", userId, auctionId);

            // 경매별 구독자 캐시에서 제거
            Set<String> subscribers = auctionSubscribers.get(auctionId);
            if (subscribers != null) {
                subscribers.remove(userId);
                int remainingSubscribers = subscribers.size();
                log.debug("[AUCTION-SUBSCRIPTION] 구독자 캐시 업데이트 - auctionId={}, remainingSubscribers={}",
                        auctionId, remainingSubscribers);

                if (subscribers.isEmpty()) {
                    auctionSubscribers.remove(auctionId);
                    log.debug("[AUCTION-SUBSCRIPTION] 경매 구독자 캐시 제거 - auctionId={}, reason=NO_SUBSCRIBERS", auctionId);
                }
            }

            long duration = System.currentTimeMillis() - startTime;
            log.info("[AUCTION-SUBSCRIPTION] 구독 해제 완료 - connectionId={}, auctionId={}, durationMs={}",
                    userId, auctionId, duration);
            return true;

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("[AUCTION-SUBSCRIPTION] 구독 해제 실패 - connectionId={}, auctionId={}, durationMs={}",
                    userId, auctionId, duration, e);
            throw new NotificationException(NotificationErrorCode.UNSUBSCRIPTION_FAIL,e);
        }
    }

    public boolean isSubscribed(String userId, Integer auctionId) {
        UserAuctionSubscription subscription = getSubscription(userId, auctionId);
        return subscription != null && subscription.isActiveSubscription();
    }

    public UserAuctionSubscription getSubscription(String connectionId, Integer auctionId) {
        Map<Integer, UserAuctionSubscription> userSubs = userSubscriptions.get(connectionId);
        if (userSubs == null) {
            return null;
        }
        return userSubs.get(auctionId);
    }
}
