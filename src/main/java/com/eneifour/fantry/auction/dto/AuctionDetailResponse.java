package com.eneifour.fantry.auction.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionDetailResponse {
    //기본 경매 or 판매 정보
    private int auctionId;
    private String saleStatus;
    private String saleType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    //상품 정보
    private int memberId;
    private String itemName;
    private String itemDescription;
    private String categoryName;
    private String artistName;
    private String albumTitle;

    //가격 정보
    private int startPrice;
    private int currentPrice;

    // current Price 는 bid 에서 조회해야 하므로 , 생성자엔 해당 인자 없이 생성
    public AuctionDetailResponse(
            int auctionId,
            int memberId,
            int startPrice,
            String saleStatus,
            String saleType,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String itemName,
            String itemDescription,
            String categoryName,
            String artistName,
            String albumTitle
    ) {
        this.auctionId = auctionId;
        this.memberId = memberId;
        this.startPrice = startPrice;
        this.saleStatus = saleStatus;
        this.saleType = saleType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.categoryName = categoryName;
        this.artistName = artistName;
        this.albumTitle = albumTitle;
    }

}
