package com.eneifour.fantry.refund.dto;

import com.eneifour.fantry.refund.domain.ReturnRequest;
import com.eneifour.fantry.refund.domain.ReturnStatus;
import java.time.LocalDateTime;

/**
 * 환불/반품 요청 목록 조회 시, 각 항목의 핵심 정보를 나타내는 요약 DTO입니다.
 * <p>상세 정보 없이 목록 표시에 필요한 최소한의 정보만을 포함합니다.
 */
public record ReturnSummaryResponse(
        int returnRequestId,
        int orderId,
        String buyerName,
        ReturnStatus status,
        LocalDateTime requestedAt
) {
    /**
     * ReturnRequest 엔티티를 목록 표시용 요약 DTO로 변환합니다.
     */
    public static ReturnSummaryResponse from(ReturnRequest returnRequest) {
        return new ReturnSummaryResponse(
                returnRequest.getReturnRequestId(),
                returnRequest.getOrders().getOrdersId(),
                returnRequest.getMember().getName(),
                returnRequest.getStatus(),
                returnRequest.getCreatedAt()
        );
    }
}