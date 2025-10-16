package com.eneifour.fantry.notification.service;

import com.eneifour.fantry.notification.dto.Notification;
import com.eneifour.fantry.notification.exception.NotificationErrorCode;
import com.eneifour.fantry.notification.exception.NotificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseConnectionService {
    @Value("${config.sse.timeout:1800000}")
    private long timeout;
    @Value("${config.sse.name:fantry-sse}")
    private String sseName;
    private final ObjectMapper objectMapper;
    private final AuctionSubscriptionService auctionSubscriptionService;
    private final Map<String, SseConnectionInfo> connections = new ConcurrentHashMap<>();
    private final Map<String, String> usernameToConnectionId = new ConcurrentHashMap<>();

    public SseEmitter createConnection(String connectionId, String username) {
        try {
            String oldConnectionId = usernameToConnectionId.get(username);
            if (oldConnectionId != null) {
                removeConnection(oldConnectionId);
            }
            SseEmitter emitter = new SseEmitter(timeout);
            SseConnectionInfo connectionInfo = new SseConnectionInfo(emitter, connectionId, username, LocalDateTime.now());
            connections.put(connectionId, connectionInfo);
            usernameToConnectionId.put(username, connectionId);
            setupEmitterCallbacks(emitter, connectionId, username);
            sendInitialMessage(emitter, connectionId);
            log.info("통합 SSE 연결 생성 완료: 사용자={}", username);
            return emitter;
        } catch (Exception e) {
            log.error("통합 SSE 연결 생성 실패: 사용자={}", username, e);
            throw new NotificationException(NotificationErrorCode.SSE_CONNECTION_FAILED, e);
        }
    }

    public void sendSubscriptionStatusNotification(String connectionId, Integer auctionId, String action, boolean success) {
        Notification notification = Notification.createNotification(Notification.NotificationType.CONNECTION_STATUS, auctionId, action + " - " + success);
        sendNotificationToConnectionId(connectionId, notification);
    }

    /**
     * 특정 username을 제외하고 경매 구독자에게 브로드캐스트 (username → connectionId 변환)
     */
    @Async
    public void broadcastToAuctionSubscribersExcludingUser(Integer auctionId, String excludeUsername, String message) {
        // username → connectionId 변환
        String excludeConnectionId = usernameToConnectionId.get(excludeUsername);

        if (excludeConnectionId == null) {
            log.debug("제외할 사용자 {} 연결 없음 - 전체 브로드캐스트", excludeUsername);
            // 연결 없으면 전체 브로드캐스트
            Notification notification = Notification.createNotification(Notification.NotificationType.BID_UPDATE, auctionId, message);
            broadcastToAuctionSubscribers(auctionId, notification);
            return;
        }

        broadcastToAuctionSubscribersExcludingConnectionId(auctionId, excludeConnectionId, message);
    }

    /**
     * 특정 connectionId를 제외하고 경매 구독자에게 브로드캐스트
     */
    @Async
    public void broadcastToAuctionSubscribersExcludingConnectionId(Integer auctionId, String excludeConnectionId, String message) {
        Set<String> subscribers = auctionSubscriptionService.getAuctionSubscribers(auctionId);
        if (subscribers.isEmpty()) {
            log.debug("경매 {} 구독자 없음", auctionId);
            return;
        }

        long targetCount = subscribers.stream()
                .filter(connectionId -> !connectionId.equals(excludeConnectionId))
                .count();

        log.info("경매 {} 선택적 통합 알림 전송 시작: {} 명 (제외 connectionId: {})", auctionId, targetCount, excludeConnectionId);

        Notification notification = Notification.createNotification(Notification.NotificationType.BID_UPDATE, auctionId, message);

        subscribers.stream()
                .filter(connectionId -> !connectionId.equals(excludeConnectionId))
                .forEach(connectionId -> {
                    try {
                        sendNotificationToConnectionId(connectionId, notification);
                    } catch (Exception e) {
                        log.warn("연결 {} 통합 알림 전송 실패", connectionId, e);
                        throw new NotificationException(NotificationErrorCode.SSE_MESSAGE_SEND_FAILED, e.getCause());
                    }
                });
    }

    @Async
    public void broadcastToAuctionSubscribers(Integer auctionId, Notification notification) {
        Set<String> subscribers = auctionSubscriptionService.getAuctionSubscribers(auctionId);
        if (subscribers.isEmpty()) {
            log.debug("경매 {} 구독자 없음", auctionId);
            return;
        }

        long targetCount = subscribers.size();
        log.info("경매 {} 선택적 통합 알림 전송 시작: {} 명", auctionId, targetCount);

        subscribers.forEach(connectionId -> {
            try {
                sendNotificationToConnectionId(connectionId, notification);
            } catch (Exception e) {
                throw new NotificationException(NotificationErrorCode.SSE_MESSAGE_SEND_FAILED, e.getCause());
            }
        });
    }

    private void sendInitialMessage(SseEmitter emitter, String connectionId) {
        try {
            // connectionId를 포함한 환영 메시지
            Notification welcomeMessage = Notification.builder()
                    .type(Notification.NotificationType.CONNECTION_STATUS.getValue())
                    .message("실시간 알림이 연결되었습니다.")
                    .connectionId(connectionId)
                    .timestamp(java.time.LocalDateTime.now())
                    .build();

            String jsonData = objectMapper.writeValueAsString(welcomeMessage);
            emitter.send(SseEmitter.event()
                    .name(sseName)
                    .data(jsonData));

            log.debug("초기 메시지 전송 완료: connectionId={}", connectionId);

        } catch (IOException e) {
            log.warn("초기 통합 메시지 전송 실패: connectionId={}", connectionId, e);
            removeConnection(connectionId);
        }
    }

    /**
     * username으로 알림 전송 (username → connectionId 변환)
     * AuctionEventListener 등에서 사용
     */
    public void sendNotificationToUser(String username, Notification notification) {
        String connectionId = usernameToConnectionId.get(username);
        if (connectionId == null) {
            log.debug("사용자 {} 통합 SSE 연결 없음", username);
            return;
        }
        sendNotificationToConnectionId(connectionId, notification);
    }

    /**
     * connectionId로 직접 알림 전송
     * 내부 로직 및 broadcast 메소드에서 사용
     */
    public void sendNotificationToConnectionId(String connectionId, Notification notification) {
        SseConnectionInfo connectionInfo = connections.get(connectionId);
        if (connectionInfo == null) {
            log.debug("연결 {} 정보 없음", connectionId);
            return;
        }

        // 구독 여부 확인 (경매 ID가 있는 경우)
        if (notification.getAuctionId() != null) {
            if (!auctionSubscriptionService.isSubscribed(connectionId, notification.getAuctionId())) {
                log.debug("연결 {} 경매 {} 구독 없음 - 알림 전송 건너뜀",
                        connectionId, notification.getAuctionId());
                return;
            }
        }

        try {
            String jsonData = objectMapper.writeValueAsString(notification);
            SseEmitter.SseEventBuilder event = SseEmitter.event()
                    .name(sseName)
                    .data(jsonData);

            connectionInfo.getEmitter().send(event);
            connectionInfo.updateLastActivity();

            log.debug("연결 {} 통합 알림 전송 완료: {}", connectionId, notification.getType());

        } catch (IOException e) {
            log.warn("연결 {} 통합 알림 전송 실패 - 연결 제거", connectionId, e);
            removeConnection(connectionId);
        } catch (Exception e) {
            log.error("연결 {} 통합 알림 처리 오류", connectionId, e);
        }
    }

    /**
     * 통합 SSE 연결 제거
     *
     * @param connectionId 연결고유 UUID
     */
    public void removeConnection(String connectionId) {
        SseConnectionInfo connectionInfo = connections.remove(connectionId);
        if (connectionInfo != null) {
            usernameToConnectionId.remove(connectionInfo.getUserId());

            try {
                connectionInfo.getEmitter().complete();
            } catch (Exception e) {
                log.debug("통합 SSE 연결 종료 중 오류: connectionId={}", connectionId, e);
                throw new NotificationException(
                        NotificationErrorCode.SSE_DISCONNECTION_FAILED, e.getCause()
                );
            }
            log.info("통합 SSE 연결 제거 완료: connectionId={}, username={}",
                    connectionId, connectionInfo.getUserId());
        }
    }

    /**
     * username으로 SSE 연결 제거 (username → connectionId 변환)
     *
     * @param username 사용자 이름
     */
    public void removeConnectionByUsername(String username) {
        String connectionId = usernameToConnectionId.get(username);
        if (connectionId != null) {
            removeConnection(connectionId);
        } else {
            log.debug("사용자 {} 연결 정보 없음", username);
        }
    }

    /**
     * 통합 SSE Emitter 콜백 설정
     */
    private void setupEmitterCallbacks(SseEmitter emitter, String connectionId, String username) {
        emitter.onCompletion(() -> {
            log.info("통합 SSE 연결 완료: username={}, connectionId={}", username, connectionId);
            removeConnection(connectionId);
            auctionSubscriptionService.unsubscribeAll(connectionId);
        });

        emitter.onTimeout(() -> {
            log.info("통합 SSE 연결 타임아웃: username={}, connectionId={}", username, connectionId);
            removeConnection(connectionId);
            auctionSubscriptionService.unsubscribeAll(connectionId);
        });

        emitter.onError(throwable -> {
            log.warn("통합 SSE 연결 오류: username={}, connectionId={}", username, connectionId, throwable);
            removeConnection(connectionId);
            auctionSubscriptionService.unsubscribeAll(connectionId);
        });
    }

    /**
     * 통합 SSE 연결 정보 내부 클래스
     */
    @Getter
    private static class SseConnectionInfo {
        private final SseEmitter emitter;
        private final String connectionId;
        private final String userId;
        private final LocalDateTime createdAt;
        private volatile LocalDateTime lastActivity;

        public SseConnectionInfo(SseEmitter emitter, String connectionId, String userId, LocalDateTime createdAt) {
            this.emitter = emitter;
            this.connectionId = connectionId;
            this.userId = userId;
            this.createdAt = createdAt;
            this.lastActivity = createdAt;
        }

        public void updateLastActivity() {
            this.lastActivity = LocalDateTime.now();
        }

        public boolean isActive() {
            return lastActivity.isAfter(LocalDateTime.now().minusMinutes(35));
        }
    }
}
