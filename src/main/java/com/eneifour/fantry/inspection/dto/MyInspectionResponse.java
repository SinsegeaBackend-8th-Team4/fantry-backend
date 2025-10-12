package com.eneifour.fantry.inspection.dto;

import com.eneifour.fantry.inspection.domain.InspectionStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class MyInspectionResponse {
    private final int productInspectionId;
    private final String itemName;
    private final String goodsCategoryName;
    private final String artistName;
    private final BigDecimal sellerHopePrice;
    private final InspectionStatus inspectionStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime submittedAt;

    // JPQL에서 바로 DTO로 조회하기 위한 생성자
    public MyInspectionResponse(int productInspectionId, String itemName, String goodsCategoryName, String artistName, BigDecimal sellerHopePrice, InspectionStatus inspectionStatus, LocalDateTime submittedAt) {
        this.productInspectionId = productInspectionId;
        this.itemName = itemName;
        this.goodsCategoryName = goodsCategoryName;
        this.artistName = artistName;
        this.sellerHopePrice = sellerHopePrice;
        this.inspectionStatus = inspectionStatus;
        this.submittedAt = submittedAt;
    }

}
