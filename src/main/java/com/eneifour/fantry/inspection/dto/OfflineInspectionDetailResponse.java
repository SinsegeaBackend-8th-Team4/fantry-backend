package com.eneifour.fantry.inspection.dto;

import com.eneifour.fantry.checklist.dto.OfflineChecklistItemResponse;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

/**
 * FE에서 관리자 페이지의 2차 오프라인 검수 상세 조회 응답 DTO
 */
@Builder
public record OfflineInspectionDetailResponse(
        OnlineInspectionDetailResponse onlineDetail,
        OnlineInspectionDetailResponse.UserInfo secondInspector,
        List<OfflineChecklistItemResponse> checklist,
        BigDecimal finalBuyPrice,
        String priceDeductionReason,
        String inspectionNotes,
        String secondRejectionReason
) {}