package com.eneifour.fantry.inspection.dto;

import com.eneifour.fantry.inspection.domain.InspectionStatus;
import com.eneifour.fantry.inspection.domain.InventoryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record InventoryListResponse (
    int productInspectionId,
    int memberId,
    String memberName,
    int goodsCategoryId,
    String goodsCategoryName,
    int artistId,
    String artistName,
    Integer albumId,
    String albumTitle,
    String itemName,
    BigDecimal expectedPrice,
    BigDecimal marketAvgPrice,
    BigDecimal sellerHopePrice,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime submittedAt,
    InspectionStatus inspectionStatus,
    InventoryStatus inventoryStatus
){}
