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
        try {

            // 기존 구독이 있는지 확인
            UserAuctionSubscription existingSubscription = getSubscription(connectionId, auctionId);
            if (existingSubscription != null && existingSubscription.isActiveSubscription()) {
                log.debug("이미 활성 구독 존재: 사용자={}, 경매={}", connectionId, auctionId);
                existingSubscription.updateLastActivity();
                return existingSubscription;
            }

            // 새로운 구독 생성
            UserAuctionSubscription subscription = UserAuctionSubscription.createSubscription(connectionId, auctionId);

            // 사용자별 구독 맵에 추가
            userSubscriptions.computeIfAbsent(connectionId, k -> new ConcurrentHashMap<>())
                    .put(auctionId, subscription);

            // 경매별 구독자 캐시에 추가
            auctionSubscribers.computeIfAbsent(auctionId, k -> ConcurrentHashMap.newKeySet())
                    .add(connectionId);

            log.info("경매 구독 완료: 사용자={}, 경매={}", connectionId, auctionId);
            return subscription;

        } catch (Exception e) {
            log.error("경매 구독 실패: 사용자={}, 경매={}", connectionId, auctionId, e);
            throw new NotificationException(NotificationErrorCode.SUBSCRIPTION_FAILED, e);
        }
    }

    public Set<String> getAuctionSubscribers(Integer auctionId) {

        Set<String> subscribers = auctionSubscribers.get(auctionId);
        if (subscribers == null) {
            return Collections.emptySet();
        }

        // 활성 구독자만 필터링
        return subscribers.stream()
                .filter(userId -> {
                    UserAuctionSubscription subscription = getSubscription(userId, auctionId);
                    return subscription != null && subscription.isActiveSubscription();
                })
                .collect(Collectors.toSet());
    }

    public int unsubscribeAll(String connectionId) {
        try {

            Map<Integer, UserAuctionSubscription> userSubs = userSubscriptions.get(connectionId);
            if (userSubs == null || userSubs.isEmpty()) {
                log.debug("구독 정보 없음: 사용자={}", connectionId);
                return 0;
            }

            int unsubscribedCount = 0;

            // 모든 구독 비활성화
            for (UserAuctionSubscription subscription : userSubs.values()) {
                if (subscription.isActiveSubscription()) {
                    subscription.deactivate();

                    // 경매별 구독자 캐시에서 제거
                    Set<String> subscribers = auctionSubscribers.get(subscription.getAuctionId());
                    if (subscribers != null) {
                        subscribers.remove(connectionId);
                        if (subscribers.isEmpty()) {
                            auctionSubscribers.remove(subscription.getAuctionId());
                        }
                    }

                    unsubscribedCount++;
                }
            }

            log.info("사용자 전체 구독 해제 완료: 사용자={}, 해제 수={}", connectionId, unsubscribedCount);
            return unsubscribedCount;

        } catch (Exception e) {
            log.error("사용자 전체 구독 해제 실패: 사용자={}", connectionId, e);
            throw new NotificationException(NotificationErrorCode.UNSUBSCRIPTION_FAIL, e);
        }
    }

    public boolean unsubscribe(String userId, Integer auctionId) {
        try {

            Map<Integer, UserAuctionSubscription> userSubs = userSubscriptions.get(userId);
            if (userSubs == null) {
                log.debug("구독 정보 없음: 사용자={}", userId);
                return false;
            }

            UserAuctionSubscription subscription = userSubs.get(auctionId);
            if (subscription == null) {
                log.debug("해당 경매 구독 없음: 사용자={}, 경매={}", userId, auctionId);
                return false;
            }

            // 구독 비활성화
            subscription.deactivate();

            // 경매별 구독자 캐시에서 제거
            Set<String> subscribers = auctionSubscribers.get(auctionId);
            if (subscribers != null) {
                subscribers.remove(userId);
                if (subscribers.isEmpty()) {
                    auctionSubscribers.remove(auctionId);
                }
            }

            log.info("경매 구독 해제 완료: 사용자={}, 경매={}", userId, auctionId);
            return true;

        } catch (Exception e) {
            log.error("경매 구독 해제 실패: 사용자={}, 경매={}", userId, auctionId, e);
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
