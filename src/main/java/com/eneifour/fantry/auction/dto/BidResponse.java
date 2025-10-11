package com.eneifour.fantry.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidResponse {

    private int bidderId;
    private String bidderName;
    private int bidAmount;
    private String itemName;
    private int itemId;
    private LocalDateTime bidAt;

}
