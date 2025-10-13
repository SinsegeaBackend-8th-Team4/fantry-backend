package com.eneifour.fantry.settlement.domain;

import com.eneifour.fantry.orders.domain.Orders;
import com.eneifour.fantry.refund.domain.ReturnRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * SettlementItem (정산 항목)
 * 개별 주문 건에 대한 정산 내역을 상세하게 기록하는 엔티티.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "settlement_item")
public class SettlementItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int settlementItemId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "settlement_id")
    private Settlement settlement;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_id")
    private ReturnRequest returnRequest;

    @Column(nullable = false)
    private BigDecimal itemSaleAmount; // 상품 판매 금액

    private BigDecimal commissionRate; // 적용된 수수료율

    private BigDecimal commissionAmount; // 수수료 금액

    @Column(nullable = false)
    private BigDecimal totalAmount; // 최종 정산 금액 (판매금액 - 수수료)

    @Column(nullable = false)
    private boolean isReturned; // 반품 여부

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}