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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionSubscriptionService {

    private final Map<String, Map<Integer, UserAuctionSubscription>> userSubscriptions = new ConcurrentHashMap<>();
    private final Map<Integer, Set<String>> auctionSubscribers = new ConcurrentHashMap<>();

    public UserAuctionSubscription subscribe(String connectionId, Integer auctionId) {
        log.info("[AUCTION-SUBSCRIPTION] 구독 시작 - connectionId={}, auctionId={}", connectionId, auctionId);

        try {
            // 원자적 구독 생성/갱신 처리
            AtomicBoolean isNewSubscription = new AtomicBoolean(false);

            Map<Integer, UserAuctionSubscription> userSubs =
                userSubscriptions.computeIfAbsent(connectionId, k -> new ConcurrentHashMap<>());

            UserAuctionSubscription subscription = userSubs.compute(auctionId, (aId, existing) -> {
                if (existing == null) {
                    isNewSubscription.set(true);
                    log.debug("[AUCTION-SUBSCRIPTION] 새 구독 생성 - connectionId={}, auctionId={}",
                        connectionId, aId);
                    return UserAuctionSubscription.createSubscription(connectionId, aId);
                } else if (!existing.isActiveSubscription()) {
                    log.info("[AUCTION-SUBSCRIPTION] 비활성 구독 재활성화 - connectionId={}, auctionId={}",
                        connectionId, aId);
                    existing.activate();
                } else {
                    log.info("[AUCTION-SUBSCRIPTION] 활성 구독 갱신 - connectionId={}, auctionId={}",
                        connectionId, aId);
                    existing.updateLastActivity();
                }
                return existing;
            });

            // 경매별 구독자 캐시에 추가 및 정합성 검증
            Set<String> subscribers = auctionSubscribers
                .computeIfAbsent(auctionId, k -> ConcurrentHashMap.newKeySet());
            boolean addedToCache = subscribers.add(connectionId);

            if (!addedToCache && isNewSubscription.get()) {
                log.warn("[AUCTION-SUBSCRIPTION] 데이터 정합성 경고 - 신규 구독이지만 캐시에 이미 존재 - connectionId={}, auctionId={}",
                    connectionId, auctionId);
            }

            log.info("[AUCTION-SUBSCRIPTION] 구독 완료 - connectionId={}, auctionId={}, isNew={}, totalSubscribers={}",
                connectionId, auctionId, isNewSubscription.get(), subscribers.size());

            return subscription;

        } catch (Exception e) {
            log.error("[AUCTION-SUBSCRIPTION] 구독 실패 - connectionId={}, auctionId={}",
                connectionId, auctionId, e);
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

            log.info("[AUCTION-SUBSCRIPTION] 전체 구독 해제 완료 - connectionId={}, totalSubscriptions={}, unsubscribedCount={}",
                    connectionId, totalSubscriptions, unsubscribedCount);
            return unsubscribedCount;

        } catch (Exception e) {
            log.error("[AUCTION-SUBSCRIPTION] 전체 구독 해제 실패 - connectionId={}",
                    connectionId, e);
            throw new NotificationException(NotificationErrorCode.UNSUBSCRIPTION_FAIL, e);
        }
    }

    public boolean unsubscribe(String userId, Integer auctionId) {
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

            log.info("[AUCTION-SUBSCRIPTION] 구독 해제 완료 - connectionId={}, auctionId={}",
                    userId, auctionId);
            return true;

        } catch (Exception e) {
            log.error("[AUCTION-SUBSCRIPTION] 구독 해제 실패 - connectionId={}, auctionId={}",
                    userId, auctionId, e);
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
