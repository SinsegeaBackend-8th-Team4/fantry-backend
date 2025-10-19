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
    private final Map<String, Thread> heartbeatThreads = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lastConnectionAttempt = new ConcurrentHashMap<>();

    public SseEmitter createConnection(String connectionId, String username) {
        log.info("[SSE-CONNECTION] 생성 시작 - username={}, connectionId={}", username, connectionId);

        try {
            // Debounce: 1초 내 중복 연결 요청 차단
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime lastAttempt = lastConnectionAttempt.get(username);
            if (lastAttempt != null && lastAttempt.plusSeconds(1).isAfter(now)) {
                log.warn("[SSE-CONNECTION] 중복 연결 요청 차단 (Debounce) - username={}, lastAttempt={}, currentAttempt={}",
                    username, lastAttempt, now);

                // 기존 연결 재사용
                String existingConnectionId = usernameToConnectionId.get(username);
                if (existingConnectionId != null) {
                    SseConnectionInfo existingConnection = connections.get(existingConnectionId);
                    if (existingConnection != null) {
                        log.info("[SSE-CONNECTION] 기존 연결 재사용 - username={}, connectionId={}",
                            username, existingConnectionId);
                        return existingConnection.getEmitter();
                    }
                }
            }
            lastConnectionAttempt.put(username, now);

            String oldConnectionId = usernameToConnectionId.get(username);
            if (oldConnectionId != null) {
                log.info("[SSE-CONNECTION] 기존 연결 감지 - username={}, oldConnectionId={}, newConnectionId={}",
                        username, oldConnectionId, connectionId);
                removeConnection(oldConnectionId);
            }

            SseEmitter emitter = new SseEmitter(timeout);
            SseConnectionInfo connectionInfo = new SseConnectionInfo(emitter, connectionId, username, LocalDateTime.now());
            connections.put(connectionId, connectionInfo);
            usernameToConnectionId.put(username, connectionId);

            log.debug("[SSE-CONNECTION] 연결 맵 저장 완료 - connectionId={}, totalConnections={}",
                    connectionId, connections.size());

            setupEmitterCallbacks(emitter, connectionId, username);
            sendInitialMessage(emitter, connectionId);
            startHeartbeat(connectionId, emitter);

            log.info("[SSE-CONNECTION] 생성 완료 - username={}, connectionId={}, totalConnections={}",
                    username, connectionId, connections.size());
            return emitter;
        } catch (Exception e) {
            log.error("[SSE-CONNECTION] 생성 실패 - username={}, connectionId={}",
                    username, connectionId, e);
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
        log.debug("[SSE-BROADCAST] 시작 (특정 사용자 제외) - auctionId={}, excludeUsername={}", auctionId, excludeUsername);

        // username → connectionId 변환
        String excludeConnectionId = usernameToConnectionId.get(excludeUsername);

        if (excludeConnectionId == null) {
            log.debug("[SSE-BROADCAST] 제외할 사용자 연결 없음 - auctionId={}, excludeUsername={}, fallbackTo=FULL_BROADCAST",
                    auctionId, excludeUsername);
            // 연결 없으면 전체 브로드캐스트
            Notification notification = Notification.createNotification(Notification.NotificationType.BID_UPDATE, auctionId, message);
            broadcastToAuctionSubscribers(auctionId, notification);
            return;
        }

        log.debug("[SSE-BROADCAST] 사용자 connectionId 매핑 - auctionId={}, excludeUsername={}, excludeConnectionId={}",
                auctionId, excludeUsername, excludeConnectionId);
        broadcastToAuctionSubscribersExcludingConnectionId(auctionId, excludeConnectionId, message);
    }

    /**
     * 특정 connectionId를 제외하고 경매 구독자에게 브로드캐스트
     */
    @Async
    public void broadcastToAuctionSubscribersExcludingConnectionId(Integer auctionId, String excludeConnectionId, String message) {
        log.info("[SSE-BROADCAST] 시작 (connectionId 제외) - auctionId={}, excludeConnectionId={}", auctionId, excludeConnectionId);

        Set<String> subscribers = auctionSubscriptionService.getAuctionSubscribers(auctionId);
        if (subscribers.isEmpty()) {
            log.debug("[SSE-BROADCAST] 구독자 없음 - auctionId={}", auctionId);
            return;
        }

        long targetCount = subscribers.stream()
                .filter(connectionId -> !connectionId.equals(excludeConnectionId))
                .count();

        log.info("[SSE-BROADCAST] 전송 시작 - auctionId={}, totalSubscribers={}, targetCount={}, excludeConnectionId={}",
                auctionId, subscribers.size(), targetCount, excludeConnectionId);

        Notification notification = Notification.createNotification(Notification.NotificationType.BID_UPDATE, auctionId, message);

        long successCount = 0;
        long failCount = 0;

        for (String connectionId : subscribers) {
            if (!connectionId.equals(excludeConnectionId)) {
                try {
                    sendNotificationToConnectionId(connectionId, notification);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    log.warn("[SSE-BROADCAST] 전송 실패 - auctionId={}, connectionId={}", auctionId, connectionId, e);
                }
            }
        }

        log.info("[SSE-BROADCAST] 완료 - auctionId={}, targetCount={}, successCount={}, failCount={}",
                auctionId, targetCount, successCount, failCount);
    }

    @Async
    public void broadcastToAuctionSubscribers(Integer auctionId, Notification notification) {
        log.info("[SSE-BROADCAST] 시작 (전체) - auctionId={}", auctionId);

        Set<String> subscribers = auctionSubscriptionService.getAuctionSubscribers(auctionId);
        if (subscribers.isEmpty()) {
            log.debug("[SSE-BROADCAST] 구독자 없음 - auctionId={}", auctionId);
            return;
        }

        long targetCount = subscribers.size();
        log.info("[SSE-BROADCAST] 전송 시작 - auctionId={}, targetCount={}", auctionId, targetCount);

        long successCount = 0;
        long failCount = 0;

        for (String connectionId : subscribers) {
            try {
                sendNotificationToConnectionId(connectionId, notification);
                successCount++;
            } catch (Exception e) {
                failCount++;
                log.warn("[SSE-BROADCAST] 전송 실패 - auctionId={}, connectionId={}", auctionId, connectionId, e);
            }
        }

        log.info("[SSE-BROADCAST] 완료 - auctionId={}, targetCount={}, successCount={}, failCount={}",
                auctionId, targetCount, successCount, failCount);
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
        log.debug("[SSE-SEND] 사용자 알림 전송 시도 - username={}, notificationType={}",
                username, notification.getType());

        String connectionId = usernameToConnectionId.get(username);
        if (connectionId == null) {
            log.debug("[SSE-SEND] 연결 없음 - username={}", username);
            return;
        }

        log.debug("[SSE-SEND] connectionId 매핑 - username={}, connectionId={}", username, connectionId);
        sendNotificationToConnectionId(connectionId, notification);
    }

    /**
     * connectionId로 직접 알림 전송
     * 내부 로직 및 broadcast 메소드에서 사용
     */
    public void sendNotificationToConnectionId(String connectionId, Notification notification) {
        log.debug("[SSE-SEND] 전송 시도 - connectionId={}, notificationType={}, auctionId={}",
                connectionId, notification.getType(), notification.getAuctionId());

        SseConnectionInfo connectionInfo = connections.get(connectionId);
        if (connectionInfo == null) {
            log.debug("[SSE-SEND] 연결 정보 없음 - connectionId={}", connectionId);
            return;
        }

        // 구독 여부 확인 (경매 ID가 있는 경우)
        if (notification.getAuctionId() != null) {
            boolean isSubscribed = auctionSubscriptionService.isSubscribed(connectionId, notification.getAuctionId());
            if (!isSubscribed) {
                log.debug("[SSE-SEND] 구독 없음 - connectionId={}, auctionId={}, skipped=true",
                        connectionId, notification.getAuctionId());
                return;
            }
            log.debug("[SSE-SEND] 구독 확인 완료 - connectionId={}, auctionId={}",
                    connectionId, notification.getAuctionId());
        }

        try {
            String jsonData = objectMapper.writeValueAsString(notification);
            SseEmitter.SseEventBuilder event = SseEmitter.event()
                    .name(sseName)
                    .data(jsonData);

            connectionInfo.getEmitter().send(event);
            connectionInfo.updateLastActivity();

            log.debug("[SSE-SEND] 전송 완료 - connectionId={}, notificationType={}",
                    connectionId, notification.getType());

        } catch (IOException e) {
            // 클라이언트 연결 끊김은 정상 상황
            log.debug("[SSE-SEND] 연결 끊김 감지 - connectionId={}, cleaning=true", connectionId);
            removeConnection(connectionId);
        } catch (Exception e) {
            log.error("[SSE-SEND] 전송 실패 - connectionId={}, notificationType={}",
                    connectionId, notification.getType(), e);
        }
    }

    /**
     * 통합 SSE 연결 제거
     *
     * @param connectionId 연결고유 UUID
     */
    public void removeConnection(String connectionId) {
        log.info("[SSE-CONNECTION-REMOVE] 시작 - connectionId={}", connectionId);

        SseConnectionInfo connectionInfo = connections.remove(connectionId);
        if (connectionInfo != null) {
            log.debug("[SSE-CONNECTION-REMOVE] 연결 정보 발견 - connectionId={}, username={}",
                    connectionId, connectionInfo.getUserId());

            // Heartbeat 중단
            stopHeartbeat(connectionId);

            int unsubscribedCount = auctionSubscriptionService.unsubscribeAll(connectionInfo.connectionId);
            log.debug("[SSE-CONNECTION-REMOVE] 구독 해제 완료 - connectionId={}, unsubscribedCount={}",
                    connectionId, unsubscribedCount);

            // usernameToConnectionId는 현재 connectionId와 일치할 때만 제거
            usernameToConnectionId.compute(connectionInfo.getUserId(), (username, currentConnectionId) -> {
                if (connectionId.equals(currentConnectionId)) {
                    log.debug("[SSE-CONNECTION-REMOVE] username 매핑 제거 - username={}, connectionId={}",
                        username, connectionId);
                    return null; // 제거
                } else {
                    log.debug("[SSE-CONNECTION-REMOVE] username 매핑 유지 (다른 연결 존재) - username={}, currentConnectionId={}, removingConnectionId={}",
                        username, currentConnectionId, connectionId);
                    return currentConnectionId; // 유지
                }
            });

            try {
                connectionInfo.getEmitter().complete();
            } catch (Exception e) {
                log.debug("[SSE-CONNECTION-REMOVE] Emitter 종료 중 예외 - connectionId={}", connectionId, e);
            }

            log.info("[SSE-CONNECTION-REMOVE] 완료 - connectionId={}, username={}, unsubscribedCount={}, remainingConnections={}",
                    connectionId, connectionInfo.getUserId(), unsubscribedCount, connections.size());
        } else {
            log.debug("[SSE-CONNECTION-REMOVE] 연결 정보 없음 (이미 제거됨) - connectionId={}", connectionId);
            // 구독 해제는 여전히 시도 (안전장치)
            int unsubscribedCount = auctionSubscriptionService.unsubscribeAll(connectionId);
            if (unsubscribedCount > 0) {
                log.debug("[SSE-CONNECTION-REMOVE] 고아 구독 정리 완료 - connectionId={}, unsubscribedCount={}",
                    connectionId, unsubscribedCount);
            }
        }
    }

    /**
     * username으로 SSE 연결 제거 (username → connectionId 변환)
     *
     * @param username 사용자 이름
     */
    public void removeConnectionByUsername(String username) {
        log.info("[SSE-CONNECTION-REMOVE-BY-USER] 시작 - username={}", username);

        String connectionId = usernameToConnectionId.get(username);
        if (connectionId != null) {
            log.debug("[SSE-CONNECTION-REMOVE-BY-USER] connectionId 매핑 - username={}, connectionId={}",
                    username, connectionId);
            removeConnection(connectionId);
        } else {
            log.debug("[SSE-CONNECTION-REMOVE-BY-USER] 연결 정보 없음 - username={}", username);
        }
    }

    /**
     * 통합 SSE Emitter 콜백 설정
     */
    private void setupEmitterCallbacks(SseEmitter emitter, String connectionId, String username) {
        emitter.onCompletion(() -> {
            log.info("[SSE-CALLBACK] onCompletion 발생 - username={}, connectionId={}", username, connectionId);
            removeConnection(connectionId); // removeConnection에서 heartbeat 중지 및 구독 해제 처리
        });

        emitter.onTimeout(() -> {
            log.warn("[SSE-CALLBACK] onTimeout 발생 - username={}, connectionId={}, timeout={}ms",
                    username, connectionId, timeout);
            removeConnection(connectionId); // removeConnection에서 heartbeat 중지 및 구독 해제 처리
        });

        emitter.onError(throwable -> {
            log.warn("[SSE-CALLBACK] onError 발생 - username={}, connectionId={}, error={}",
                    username, connectionId, throwable.getMessage(), throwable);
            removeConnection(connectionId); // removeConnection에서 heartbeat 중지 및 구독 해제 처리
        });
    }

    /**
     * Virtual Thread 기반 Heartbeat 시작
     * 30초마다 comment 이벤트를 전송하여 연결 유지
     */
    private void startHeartbeat(String connectionId, SseEmitter emitter) {
        Thread heartbeatThread = Thread.startVirtualThread(() -> {
            log.debug("[SSE] Heartbeat 시작: connectionId={}", connectionId);
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(30000); // 30초 대기 (Virtual Thread는 블로킹하지 않음)
                    emitter.send(SseEmitter.event().comment("heartbeat"));
                    log.debug("[SSE] Heartbeat 전송 성공: connectionId={}", connectionId);
                } catch (InterruptedException e) {
                    log.debug("[SSE] Heartbeat 중단됨: connectionId={}", connectionId);
                    Thread.currentThread().interrupt(); // Interrupt 상태 복원
                    break;
                } catch (IOException e) {
                    // 클라이언트 연결 끊김은 정상 상황 - debug 레벨로 처리
                    log.debug("[SSE] 클라이언트 연결 끊김, 정리 중: connectionId={}", connectionId);
                    removeConnection(connectionId);
                    break;
                } catch (Exception e) {
                    // 그 외 예외는 warn 레벨로 기록
                    log.warn("[SSE] Heartbeat 전송 실패, 연결 제거: connectionId={}", connectionId, e);
                    removeConnection(connectionId);
                    break;
                }
            }
            log.debug("[SSE] Heartbeat 종료: connectionId={}", connectionId);
        });
        heartbeatThreads.put(connectionId, heartbeatThread);
    }

    /**
     * Virtual Thread Heartbeat 중단
     */
    private void stopHeartbeat(String connectionId) {
        Thread thread = heartbeatThreads.remove(connectionId);
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            log.debug("[SSE] Heartbeat 중지 완료: connectionId={}", connectionId);
        }
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
