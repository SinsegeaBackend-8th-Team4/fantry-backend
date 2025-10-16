package com.eneifour.fantry.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubscriptionRequest {
    /**
     * 사용자 ID
     */
    @NotBlank(message = "사용자 ID는 필수입니다")
    private String username;

    /**
     * 경매 ID
     */
    @NotNull(message = "경매 ID는 필수입니다")
    private Integer auctionId;

    /**
     * 액션 유형 (subscribe/unsubscribe)
     */
    @NotBlank(message = "액션은 필수입니다")
    @Pattern(regexp = "^(subscribe|unsubscribe)$", message = "액션은 subscribe 또는 unsubscribe만 가능합니다")
    private String action;

    /**
     * 구독 액션인지 확인
     * @return 구독 액션 여부
     */
    public boolean isSubscribeAction() {
        return "subscribe".equals(action);
    }

    /**
     * 구독 해제 액션인지 확인
     * @return 구독 해제 액션 여부
     */
    public boolean isUnsubscribeAction() {
        return "unsubscribe".equals(action);
    }

    /**
     * 액션 유형 열거형
     */
    public enum ActionType {
        SUBSCRIBE("subscribe"),
        UNSUBSCRIBE("unsubscribe");

        private final String value;

        ActionType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static ActionType fromValue(String value) {
            for (ActionType type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid action type: " + value);
        }
    }
}
