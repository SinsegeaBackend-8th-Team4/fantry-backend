package com.eneifour.fantry.refund.dto;

import com.eneifour.fantry.refund.domain.ReturnStatus;

import java.util.Map;

public record ReturnStatsAdminResponse(
        long requested,
        long inTransit,
        long inspecting,
        long approved,
        long rejected,
        long completed,
        long userCancelled,
        long total
) {
    public static ReturnStatsAdminResponse from(Map<ReturnStatus, Long> statusCounts) {
        long requested = statusCounts.getOrDefault(ReturnStatus.REQUESTED, 0L);
        long inTransit = statusCounts.getOrDefault(ReturnStatus.IN_TRANSIT, 0L);
        long inspecting = statusCounts.getOrDefault(ReturnStatus.INSPECTING, 0L);
        long approved = statusCounts.getOrDefault(ReturnStatus.APPROVED, 0L);
        long rejected = statusCounts.getOrDefault(ReturnStatus.REJECTED, 0L);
        long completed = statusCounts.getOrDefault(ReturnStatus.COMPLETED, 0L);
        long userCancelled = statusCounts.getOrDefault(ReturnStatus.USER_CANCELLED, 0L);

        // DELETED 상태는 통계에 포함하지 않음
        long total = requested + inTransit + inspecting + approved + rejected + completed + userCancelled;

        return new ReturnStatsAdminResponse(requested, inTransit, inspecting, approved, rejected, completed, userCancelled, total);
    }
}
