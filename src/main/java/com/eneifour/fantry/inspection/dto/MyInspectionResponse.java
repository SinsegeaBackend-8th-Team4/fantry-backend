package com.eneifour.fantry.inspection.dto;

import com.eneifour.fantry.inspection.domain.InspectionStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyInspectionResponse {
    private int productInspectionId;
    private String itemName;
    private String itemDescription;
    private String goodsCategoryName;
    private String artistName;
    private BigDecimal sellerHopePrice;
    private BigDecimal finalBuyPrice;
    private InspectionStatus inspectionStatus;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime submittedAt;
    private String firstRejectionReason;
    private String secondRejectionReason;
    private String priceDeductionReason;
}

