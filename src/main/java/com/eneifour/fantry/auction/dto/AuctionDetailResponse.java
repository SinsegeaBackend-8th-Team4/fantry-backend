package com.eneifour.fantry.auction.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionDetailResponse {
    private int auctionId;
    private int memberId;
    private int price;
    private String saleStatus;
    private String saleType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String itemName;
    private String itemDescription;
    private String categoryName;
    private String artistName;
    private String albumTitle;
}
