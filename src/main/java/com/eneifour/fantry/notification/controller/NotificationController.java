package com.eneifour.fantry.notification.controller;

import com.eneifour.fantry.notification.domain.UserAuctionSubscription;
import com.eneifour.fantry.notification.dto.*;
import com.eneifour.fantry.notification.service.AuctionSubscriptionService;
import com.eneifour.fantry.notification.service.SseConnectionService;
import com.eneifour.fantry.security.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationController {

    private final SseConnectionService sseConnectionService;
    private final AuctionSubscriptionService auctionSubscriptionService;

    @GetMapping(value = "/notification/sse/{username}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connection(
            @PathVariable("username") String username,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) throws IOException {
        log.info("[SSE] 연결 요청 시작 - username={}", username);

        SseEmitter emitter = new SseEmitter(1800000L);
        if (customUserDetails == null || !customUserDetails.isEnabled()) {
            log.warn("[SSE] 인증 실패 - username={}, reason=UNAUTHENTICATED", username);
            emitter.send(SseEmitter.event()
                    .id("error")
                    .name("auth-error")
                    .data("{\n\"code\": 401,\n\"message\": \"인증되지 않은 사용자\"\n}"));
            emitter.complete();
            return emitter;
        }

        if(!customUserDetails.getUsername().equals(username)){
            log.warn("[SSE] 인증 실패 - username={}, authenticatedUser={}, reason=FORBIDDEN",
                    username, customUserDetails.getUsername());
            emitter.send(SseEmitter.event()
                    .id("error")
                    .name("auth-error")
                    .data("{\n\"code\": 403,\n\"message\": \"접근 권한이 없습니다\"\n}"));
            emitter.complete();
            return emitter;
        }
        String connectionId = UUID.randomUUID().toString();
        log.info("[SSE] 연결 생성 중 - username={}, connectionId={}", username, connectionId);
        return sseConnectionService.createConnection(connectionId, username);
    }

    @PreAuthorize("!isAnonymous()")
    @PostMapping("/notification/subscriptions")
    public ResponseEntity<ApiResponse<SubscriptionResponse>> manageSubscription(
            @Valid @RequestBody SubscriptionRequest request,
            @RequestHeader(value = "X-Connection-Id", required = false) String connectionId) {
        log.info("[SSE-SUBSCRIPTION] 요청 시작 - username={}, auctionId={}, action={}, connectionId={}",
                request.getUsername(), request.getAuctionId(), request.getAction(), connectionId);

        // Connection ID 검증
        if (connectionId == null || connectionId.isEmpty()) {
            log.warn("[SSE-SUBSCRIPTION] 실패 - username={}, auctionId={}, reason=NO_CONNECTION_ID",
                    request.getUsername(), request.getAuctionId());
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, null));
        }

        boolean success = false;
        String message = "";
        UserAuctionSubscription subscription = null;
        long startTime = System.currentTimeMillis();

        try {
            if (request.isSubscribeAction()) {
                log.debug("[SSE-SUBSCRIPTION] 구독 처리 시작 - connectionId={}, auctionId={}",
                        connectionId, request.getAuctionId());
                // 구독 처리 - connectionId 사용
                subscription = auctionSubscriptionService.subscribe(connectionId, request.getAuctionId());
                success = subscription != null;
                message = success ? "경매 구독이 완료되었습니다" : "경매 구독에 실패했습니다";

            } else if (request.isUnsubscribeAction()) {
                log.debug("[SSE-SUBSCRIPTION] 구독 해제 처리 시작 - connectionId={}, auctionId={}",
                        connectionId, request.getAuctionId());
                // 구독 해제 처리 - connectionId 사용
                success = auctionSubscriptionService.unsubscribe(connectionId, request.getAuctionId());
                message = success ? "경매 구독이 해제되었습니다" : "경매 구독 해제에 실패했습니다";
            }

            SubscriptionResponse response = SubscriptionResponse.builder()
                    .success(success)
                    .message(message)
                    .username(request.getUsername())
                    .auctionId(request.getAuctionId())
                    .action(request.getAction())
                    .subscriptionInfo(UserAuctionSubscriptionDto.from(subscription))
                    .build();

            long duration = System.currentTimeMillis() - startTime;
            log.info("[SSE-SUBSCRIPTION] 완료 - username={}, connectionId={}, auctionId={}, action={}, success={}, durationMs={}",
                    request.getUsername(), connectionId, request.getAuctionId(), request.getAction(), success, duration);

            return ResponseEntity.ok(new ApiResponse<>(true, response));
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("[SSE-SUBSCRIPTION] 예외 발생 - username={}, connectionId={}, auctionId={}, action={}, durationMs={}",
                    request.getUsername(), connectionId, request.getAuctionId(), request.getAction(), duration, e);
            throw e;
        }
    }

    @DeleteMapping("/notification/{username}")
    public ResponseEntity<ApiResponse<DisconnectResponse>> disconnect(@PathVariable String username) {
        log.info("[SSE-DISCONNECT] 요청 시작 - username={}", username);
        long startTime = System.currentTimeMillis();

        try {
            // username으로 connectionId 찾아서 제거
            sseConnectionService.removeConnectionByUsername(username);

            // 참고: 구독 해제는 SseEmitter의 onCompletion 콜백에서 자동으로 처리됨
            long duration = System.currentTimeMillis() - startTime;
            log.info("[SSE-DISCONNECT] 완료 - username={}, durationMs={}", username, duration);
            return ResponseEntity.ok(new ApiResponse<>(true, new DisconnectResponse("통합 연결이 해제되었습니다", username, 0)));
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("[SSE-DISCONNECT] 예외 발생 - username={}, durationMs={}", username, duration, e);
            throw e;
        }
    }

    @PostMapping("/notification/test/{username}")
    public ResponseEntity<Map<String, String>> sendTestUnifiedMessage(
            @PathVariable String username,
            @RequestBody Map<String, String> request) {

        log.info("[SSE-TEST] 요청 시작 - username={}", username);
        long startTime = System.currentTimeMillis();

        try {
            String message = request.getOrDefault("message", "테스트 통합 메시지");
            log.debug("[SSE-TEST] 메시지 생성 - username={}, message={}", username, message);

            // 테스트 통합 알림 생성 및 전송
            Notification testNotification = Notification.createNotification(Notification.NotificationType.BID_UPDATE, message);
            sseConnectionService.sendNotificationToUser(username, testNotification);

            long duration = System.currentTimeMillis() - startTime;
            log.info("[SSE-TEST] 완료 - username={}, durationMs={}", username, duration);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "테스트 통합 메시지가 전송되었습니다",
                    "userId", username,
                    "testMessage", message
            ));

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("[SSE-TEST] 예외 발생 - username={}, durationMs={}", username, duration, e);
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "테스트 통합 메시지 전송 실패: " + e.getMessage(),
                    "userId", username
            ));
        }
    }
}
