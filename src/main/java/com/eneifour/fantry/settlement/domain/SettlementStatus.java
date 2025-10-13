package com.eneifour.fantry.settlement.domain;

/**
 * 정산 상태를 나타내는 Enum.
 * PENDING: 정산 대기
 * PAID: 지급 완료
 * CANCELLED: 정산 취소
 * FAILED: 정산 실패
 */
public enum SettlementStatus {
    PENDING,
    PAID,
    CANCELLED,
    FAILED
}