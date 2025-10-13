package com.eneifour.fantry.settlement.domain;

/**
 * 매출 장부의 수익/비용 유형을 나타내는 Enum.
 * COMMISSION: 판매 수수료 수익
 * DIRECT_PURCHASE_PROFIT: 직매입 상품 판매 수익
 * CANCELLATION_FEE: 취소 수수료 수익
 * REFUND_DEDUCTION: 환불 시 차감액 (e.g., 반품 배송비)
 * REFUND_LOSS: 환불로 인한 플랫폼 손실
 */
public enum RevenueType {
    COMMISSION,
    DIRECT_PURCHASE_PROFIT,
    CANCELLATION_FEE,
    REFUND_DEDUCTION,
    REFUND_LOSS
}