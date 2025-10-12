package com.eneifour.fantry.refund.dto;

import com.eneifour.fantry.refund.domain.ReturnStatus;
import com.eneifour.fantry.refund.domain.ReturnStatusHistory;

import java.time.LocalDateTime;

/**
 * 환불/반품 요청의 상태 변경 이력 한 건을 나타내는 응답 DTO입니다.
 * <p>상세 응답 DTO에 포함되어, 언제, 누구에 의해, 어떤 상태로 변경되었는지 보여주는 데 사용됩니다.
 */
public record ReturnHistoryResponse(
        ReturnStatus previousStatus,
        ReturnStatus newStatus,
        String updatedBy,
        LocalDateTime updatedAt,
        String memo
) {
    /**
     * ReturnStatusHistory 엔티티를 응답용 DTO로 변환합니다.
     * <p>상태 변경 주체(updatedBy)가 없는 경우 'System'으로 표시합니다.
     */
    public static ReturnHistoryResponse from(ReturnStatusHistory history) {
        String updatedBy = (history.getUpdatedBy() != null) ? history.getUpdatedBy().getName() : "System";
        return new ReturnHistoryResponse(
                history.getPreviousStatus(),
                history.getNewStatus(),
                updatedBy,
                history.getUpdatedAt(),
                history.getMemo()
        );
    }
}