package com.eneifour.fantry.refund.dto;

import com.eneifour.fantry.refund.domain.ReturnReason;
import com.eneifour.fantry.refund.domain.ReturnRequest;
import com.eneifour.fantry.refund.domain.ReturnStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자에게 보여줄 환불/반품 요청의 상세 정보를 담는 DTO입니다.
 * <p>관리자용 응답과 달리, 처리자 정보나 내부 메모 등 민감한 정보는 제외됩니다.
 */
public record ReturnDetailResponse(
        int returnRequestId,
        int orderId,
        ReturnReason reason,
        String detailReason,
        ReturnStatus status,
        BigDecimal finalRefundAmount,
        LocalDateTime requestedAt,
        LocalDateTime completedAt,
        List<String> attachmentUrls,
        List<ReturnHistoryResponse> statusHistories
) {
    /**
     * ReturnRequest 엔티티와 첨부파일 URL 목록을 기반으로 사용자용 상세 응답 DTO를 생성합니다.
     */
    public static ReturnDetailResponse from(ReturnRequest returnRequest, List<String> urls) {
        List<ReturnHistoryResponse> histories = returnRequest.getStatusHistories().stream()
                .map(ReturnHistoryResponse::from)
                .collect(Collectors.toList());

        return new ReturnDetailResponse(
                returnRequest.getReturnRequestId(),
                returnRequest.getOrders().getOrdersId(),
                returnRequest.getReason(),
                returnRequest.getDetailReason(),
                returnRequest.getStatus(),
                returnRequest.getFinalRefundAmount(),
                returnRequest.getCreatedAt(),
                returnRequest.getCompletedAt(),
                urls,
                histories
        );
    }
}
