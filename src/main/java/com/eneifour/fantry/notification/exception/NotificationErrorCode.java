package com.eneifour.fantry.notification.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotificationErrorCode {
    // SSE Connection Exceptions
    SSE_CONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "N001", "SSE 연결 생성에 실패했습니다."),
    SSE_DISCONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "N004", "SSE 연결 해제에 실패했습니다."),
    SSE_MESSAGE_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "N002", "SSE 메시지 전송에 실패했습니다."),
    SSE_CONNECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "N003", "SSE 연결을 찾을 수 없습니다."),
    // Subscription Exceptions
    SUBSCRIPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "N101", "구독 처리에 실패했습니다."),
    ALREADY_SUBSCRIBED(HttpStatus.CONFLICT, "N102", "이미 구독 중인 경매입니다."),
    SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "N103", "구독 정보를 찾을 수 없습니다."),
    UNSUBSCRIPTION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "N104", "구독 해제에 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}