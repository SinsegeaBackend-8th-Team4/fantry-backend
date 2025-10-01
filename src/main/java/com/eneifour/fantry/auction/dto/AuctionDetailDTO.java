package com.eneifour.fantry.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionDetailDTO {
    private int auctionId;
    private int memberId;
    private int price;
    private String saleStatus;
    private String sale_type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String itemName;
    private String itemDescription;
    private String categoryName;
    private String artistName;
    private String albumTitle;

    public AuctionDetailDTO(int auctionId, int memberId, int price, String saleStatus, String sale_type, String itemName, String itemDescription, String categoryName, String artistName, String albumTitle, LocalDateTime startTime, LocalDateTime endTime ) {
        this.auctionId = auctionId;
        this.memberId = memberId;
        this.price = price;
        this.saleStatus = saleStatus;
        this.sale_type = sale_type;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.categoryName = categoryName;
        this.artistName = artistName;
        this.albumTitle = albumTitle;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
