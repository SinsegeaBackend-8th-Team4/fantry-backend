package com.eneifour.fantry.bid.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BidRequest {
    private int memberId;
    private int bidAmount;
    private LocalDateTime bidAt;
}
