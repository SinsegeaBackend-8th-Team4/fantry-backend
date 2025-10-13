package com.eneifour.fantry.refund.dto;

import com.eneifour.fantry.refund.domain.ReturnReason;
import com.eneifour.fantry.refund.domain.ReturnRequest;
import com.eneifour.fantry.refund.domain.ReturnStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 관리자에게 보여줄 환불/반품 요청의 모든 상세 정보를 담는 DTO입니다.
 * <p>사용자 정보, 처리자 정보, 내부 메모 등 관리자에게만 필요한 정보들을 포함합니다.
 */
public record ReturnAdminResponse(
        int returnRequestId,
        int orderId,
        String buyerName,
        String createdBy,
        String updatedBy,
        ReturnReason reason,
        String detailReason,
        ReturnStatus status,
        BigDecimal originalPaymentAmount,
        BigDecimal deductedShippingFee,
        BigDecimal finalRefundAmount,
        String rejectReason,
        String comment,
        LocalDateTime requestedAt,
        LocalDateTime completedAt,
        List<String> attachmentUrls,
        List<ReturnHistoryResponse> statusHistories
) {
    /**
     * ReturnRequest 엔티티와 첨부파일 URL 목록을 기반으로 관리자용 상세 응답 DTO를 생성합니다.
     */
    public static ReturnAdminResponse from(ReturnRequest returnRequest, List<String> urls) {
        List<ReturnHistoryResponse> histories = returnRequest.getStatusHistories().stream()
                .map(ReturnHistoryResponse::from)
                .collect(Collectors.toList());

        String createdByName = (returnRequest.getCreatedBy() != null) ? returnRequest.getCreatedBy().getName() : null;
        String updatedByName = (returnRequest.getUpdatedBy() != null) ? returnRequest.getUpdatedBy().getName() : null;

        return new ReturnAdminResponse(
                returnRequest.getReturnRequestId(),
                returnRequest.getOrders().getOrdersId(),
                returnRequest.getMember().getName(),
                createdByName,
                updatedByName,
                returnRequest.getReason(),
                returnRequest.getDetailReason(),
                returnRequest.getStatus(),
                returnRequest.getOriginalPaymentAmount(),
                returnRequest.getDeductedShippingFee(),
                returnRequest.getFinalRefundAmount(),
                returnRequest.getRejectReason(),
                returnRequest.getComment(),
                returnRequest.getCreatedAt(),
                returnRequest.getCompletedAt(),
                urls,
                histories
        );
    }
}
