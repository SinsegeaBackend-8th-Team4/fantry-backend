package com.eneifour.fantry.settlement.domain;

import com.eneifour.fantry.orders.domain.Orders;
import com.eneifour.fantry.refund.domain.ReturnRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * RevenueLedger (매출 장부)
 * 플랫폼에서 발생하는 모든 수익과 비용의 흐름을 기록하는 엔티티.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "revenue_ledger")
public class RevenueLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ledgerId;

    @Column(nullable = false)
    private LocalDateTime transactionAt; // 거래 발생 시간

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RevenueType revenueType; // 수익/비용 유형

    @Column(nullable = false)
    private BigDecimal amount; // 금액 (수익: +, 비용: -)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_order_id")
    private Orders relatedOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_return_id")
    private ReturnRequest relatedReturn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_settlement_id")
    private Settlement relatedSettlement;

    private String description; // 거래 내용 요약
}