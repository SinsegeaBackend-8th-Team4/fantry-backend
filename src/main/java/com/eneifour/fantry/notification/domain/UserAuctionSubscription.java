package com.eneifour.fantry.notification.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사용자 경매 구독 도메인 모델
 * 사용자가 구독한 경매 정보를 관리
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAuctionSubscription {
    
    /**
     * 연결 ID
     */
    private String connectionId;
    
    /**
     * 경매 ID
     */
    private Integer auctionId;
    
    /**
     * 구독 시작 시간
     */
    private LocalDateTime subscribedAt;
    
    /**
     * 구독 활성 상태
     */
    private boolean active;
    
    /**
     * 마지막 활동 시간 (heartbeat 용도)
     */
    private LocalDateTime lastActivityAt;
    
    /**
     * 구독 비활성화
     */
    public void deactivate() {
        this.active = false;
        this.lastActivityAt = LocalDateTime.now();
    }
    
    /**
     * 구독 활성화
     */
    public void activate() {
        this.active = true;
        this.lastActivityAt = LocalDateTime.now();
    }
    
    /**
     * 마지막 활동 시간 업데이트
     */
    public void updateLastActivity() {
        this.lastActivityAt = LocalDateTime.now();
    }
    
    /**
     * 활성 구독 여부 확인
     * @return 활성 상태인지 여부
     */
    public boolean isActiveSubscription() {
        return active && lastActivityAt != null && 
               lastActivityAt.isAfter(LocalDateTime.now().minusMinutes(35));
    }
    
    /**
     * 새로운 구독 생성 팩토리 메서드
     * @param connectionId 사용자 ID
     * @param auctionId 경매 ID
     * @return 새로운 구독 객체
     */
    public static UserAuctionSubscription createSubscription(String connectionId, Integer auctionId) {
        LocalDateTime now = LocalDateTime.now();
        return UserAuctionSubscription.builder()
                .connectionId(connectionId)
                .auctionId(auctionId)
                .subscribedAt(now)
                .lastActivityAt(now)
                .active(true)
                .build();
    }
}