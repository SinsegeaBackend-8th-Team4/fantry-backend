package com.eneifour.fantry.inspection.dto;

import com.eneifour.fantry.inspection.domain.InspectionStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * FE에서 관리자 페이지의 검수 리스트 조회 응답 DTO
 */
@Builder
public record InspectionListResponse(
        int productInspectionId,
        String submissionUuid,
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
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime submittedAt,
        InspectionStatus inspectionStatus
) { }