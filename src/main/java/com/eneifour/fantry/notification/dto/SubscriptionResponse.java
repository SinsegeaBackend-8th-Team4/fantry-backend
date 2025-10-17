package com.eneifour.fantry.notification.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionResponse {
    private boolean success;
    private String message;
    private String username;
    private Integer auctionId;
    private String action;
    private UserAuctionSubscriptionDto subscriptionInfo;
}
