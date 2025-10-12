package com.eneifour.fantry.bid.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
