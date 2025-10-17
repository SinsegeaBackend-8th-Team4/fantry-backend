package com.eneifour.fantry.notification.dto;

import com.eneifour.fantry.notification.domain.UserAuctionSubscription;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAuctionSubscriptionDto {
    private String username;
    private Integer auctionId;
    private LocalDateTime subscribeAt;

    public static UserAuctionSubscriptionDto from(UserAuctionSubscription userAuctionSubscription) {
        if (userAuctionSubscription == null) {
            return null;
        }
        return UserAuctionSubscriptionDto.builder()
                .username(userAuctionSubscription.getConnectionId())
                .auctionId(userAuctionSubscription.getAuctionId())
                .subscribeAt(userAuctionSubscription.getSubscribedAt())
                .build();
    }
}
