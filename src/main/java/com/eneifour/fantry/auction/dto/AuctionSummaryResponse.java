package com.eneifour.fantry.auction.dto;

import com.eneifour.fantry.auction.domain.Auction;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // 초기화하지 않은 필드는 JSON 변환 시 제외
public class AuctionSummaryResponse {

    private int auctionId;
    private int itemId;
    private String itemName;
    private String categoryName; // 카테고리명 필드 추가
    private String artistGroupType;
    private int startPrice;
    private int currentPrice; // Redis에서 조회한 실시간 현재가
    private int highestBidderId; // Redis 에서 조회한 최고가 입찰자 (없는경우 0)
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String saleType;
    private String saleStatus;
    private List fileInfos;


    public static AuctionSummaryResponse from(Auction auction) {
        return AuctionSummaryResponse.builder()
                .auctionId(auction.getAuctionId())
                .itemId(auction.getProductInspection().getProductInspectionId())
                .itemName(auction.getProductInspection().getItemName())
                .startPrice(auction.getStartPrice())
                .startTime(auction.getStartTime())
                .endTime(auction.getEndTime())
                .saleType(auction.getSaleType().toString())
                .saleStatus(auction.getSaleStatus().toString())
                .build();
    }
}
