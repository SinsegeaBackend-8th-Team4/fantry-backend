package com.eneifour.fantry.auction.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BidDTO {
    private int memberId;
    private int bidAmount;
    private LocalDateTime bidAt;

}
