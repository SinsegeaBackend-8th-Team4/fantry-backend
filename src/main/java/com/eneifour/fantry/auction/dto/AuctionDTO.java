package com.eneifour.fantry.auction.dto;

import com.eneifour.fantry.auction.domain.Auction;
import lombok.Data;

@Data
public class AuctionDTO {
    int auctionId;
    int memberId;
    String itemName;
    String itemDescription;
    int price;
    String categoryName;
    String artistName;
    String albumTitle;

    public AuctionDTO(Auction a){
        this.auctionId = a.getAuctionId();
        this.memberId = a.getInventoryItem().getMember().getMemberId();
        this.itemName = a.getInventoryItem().getItemName();
        this.itemDescription = a.getInventoryItem().getItemDescription();
        this.price = a.getStartPrice();
        this.categoryName = a.getInventoryItem().getCategory();
        this.artistName = a.getInventoryItem().getArtist();
        this.albumTitle = a.getInventoryItem().getAlbum();
    }
}
