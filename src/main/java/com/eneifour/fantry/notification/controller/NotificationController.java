package com.eneifour.fantry.notification.controller;

import com.eneifour.fantry.notification.domain.UserAuctionSubscription;
import com.eneifour.fantry.notification.dto.*;
import com.eneifour.fantry.notification.service.AuctionSubscriptionService;
import com.eneifour.fantry.notification.service.SseConnectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationController {

    private final SseConnectionService sseConnectionService;
    private final AuctionSubscriptionService auctionSubscriptionService;

    @PreAuthorize("#username == authentication.principal.username and !isAnonymous()")
    @GetMapping(value = "/notification/{username}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connection(
            @PathVariable("username") String username
    ) {
        String connectionId = UUID.randomUUID().toString();
        return sseConnectionService.createConnection(connectionId, username);
    }

    @PreAuthorize("!isAnonymous()")
    @PostMapping("/notification/subscriptions")
    public ResponseEntity<ApiResponse<SubscriptionResponse>> manageSubscription(
            @Valid @RequestBody SubscriptionRequest request,
            @RequestHeader(value = "X-Connection-Id", required = false) String connectionId) {
        log.info("구독 관리 요청: 사용자={}, 경매={}, 액션={}, connectionId={}",
                request.getUsername(), request.getAuctionId(), request.getAction(), connectionId);

        // Connection ID 검증
        if (connectionId == null || connectionId.isEmpty()) {
            log.warn("Connection ID 없음: 사용자={}", request.getUsername());
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, null));
        }

        boolean success = false;
        String message = "";
        UserAuctionSubscription subscription = null;

        if (request.isSubscribeAction()) {
            // 구독 처리 - connectionId 사용
            subscription = auctionSubscriptionService.subscribe(connectionId, request.getAuctionId());
            success = subscription != null;
            message = success ? "경매 구독이 완료되었습니다" : "경매 구독에 실패했습니다";

        } else if (request.isUnsubscribeAction()) {
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

        log.info("구독 관리 완료: 사용자={}, connectionId={}, 경매={}, 액션={}, 성공={}",
                request.getUsername(), connectionId, request.getAuctionId(), request.getAction(), success);

        return ResponseEntity.ok(new ApiResponse<>(true, response));
    }

    @DeleteMapping("/notification/{username}")
    public ResponseEntity<ApiResponse<DisconnectResponse>> disconnect(@PathVariable String username) {
        log.info("통합 SSE 연결 해제 요청: 사용자={}", username);

        // username으로 connectionId 찾아서 제거
        sseConnectionService.removeConnectionByUsername(username);

        // 참고: 구독 해제는 SseEmitter의 onCompletion 콜백에서 자동으로 처리됨
        log.info("통합 SSE 연결 해제 완료: 사용자={}", username);
        return ResponseEntity.ok(new ApiResponse<>(true, new DisconnectResponse("통합 연결이 해제되었습니다", username, 0)));
    }

    @PostMapping("/notification/test/{username}")
    public ResponseEntity<Map<String, String>> sendTestUnifiedMessage(
            @PathVariable String username,
            @RequestBody Map<String, String> request) {

        log.info("테스트 통합 메시지 전송 요청: 사용자={}", username);

        try {
            String message = request.getOrDefault("message", "테스트 통합 메시지");
            // 테스트 통합 알림 생성 및 전송
            Notification testNotification = Notification.createNotification(Notification.NotificationType.BID_UPDATE, message);
            sseConnectionService.sendNotificationToUser(username, testNotification);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "테스트 통합 메시지가 전송되었습니다",
                    "userId", username,
                    "testMessage", message
            ));

        } catch (Exception e) {
            log.error("테스트 통합 메시지 전송 실패: 사용자={}", username, e);
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "테스트 통합 메시지 전송 실패: " + e.getMessage(),
                    "userId", username
            ));
        }
    }
}
