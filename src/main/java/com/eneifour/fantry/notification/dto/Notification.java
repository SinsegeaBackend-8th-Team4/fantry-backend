package com.eneifour.fantry.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 통합 알림 DTO
 * 통합 SSE 연결에서 사용되는 범용 알림 메시지
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notification {
    
    /**
     * 알림 유형
     */
    private String type;

    /**
     * 경매 ID
     */
    private Integer auctionId;

    /**
     * 알림 생성 시간
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * 메시지 내용
     */
    private String message;

    /**
     * SSE Connection ID (초기 연결 메시지에서만 사용)
     */
    private String connectionId;
    
    /**
     * 입찰 알림 생성
     * @param auctionId 경매 ID
     * @param message 알림 메시지
     * @return 통합 알림 객체
     */
    public static Notification createNotification(NotificationType notificationType, Integer auctionId, String message) {
        return Notification.builder()
                .type(notificationType.getValue())
                .auctionId(auctionId)
                .timestamp(LocalDateTime.now())
                .message(message)
                .build();
    }

    /**
     * 입찰 알림 생성
     * @param message 알림 메시지
     * @return 통합 알림 객체
     */
    public static Notification createNotification(NotificationType notificationType, String message) {
        return Notification.builder()
                .type(notificationType.getValue())
                .timestamp(LocalDateTime.now())
                .message(message)
                .build();
    }
    
    /**
     * 통합 알림 유형 열거형
     */
    public enum NotificationType {
        BID_UPDATE("bid_update"),
        BID_SUCCESSFUL("bid_successful"),
        AUCTION_END("auction_end"),
        AUCTION_START("auction_start"),
        CONNECTION_STATUS("connection_status");
        
        private final String value;
        
        NotificationType(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public static NotificationType fromValue(String value) {
            for (NotificationType type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid notification type: " + value);
        }
    }
}