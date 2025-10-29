package com.eneifour.fantry.settlement.domain;

import com.eneifour.fantry.account.domain.Account;
import com.eneifour.fantry.member.domain.Member;
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
import java.util.ArrayList;
import java.util.List;

/**
 * com.eneifour.fantry.settlement.domain.Settlement (정산)
 * 특정 판매자의 정산 요청 한 건에 대한 정보를 담는 엔티티.
 * 하나의 정산(com.eneifour.fantry.settlement.domain.Settlement)은 여러 개의 정산 항목(SettlementItem)으로 구성된다.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "settlement")
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int settlementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 정산 대상 판매자

    @Column(nullable = false)
    private BigDecimal totalAmount; // 총 판매 금액 (수수료 차감 전)

    private BigDecimal commissionAmount; // 수수료 금액

    @Column(nullable = false)
    private BigDecimal settlementAmount; // 실제 지급될 정산 금액

    private BigDecimal totalReturnAmount; // 총 반품 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SettlementStatus status; // 정산 상태

    @Lob
    private String failureReason; // 정산 실패 사유

    @Column(nullable = false)
    private LocalDateTime requestedAt; // 정산 요청(생성) 시간

    private LocalDateTime completedAt; // 정산 지급 완료 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account; // 정산 받을 계좌

    @OneToMany(mappedBy = "settlement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SettlementItem> settlementItems = new ArrayList<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", updatable = false)
    private Member createdBy;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private Member updatedBy;

    /**
     * 정산 완료 시 최종 정산 정보를 업데이트합니다.
     * @param totalSalesAmount 총 판매 금액
     * @param totalCommissionAmount 총 수수료 금액
     * @param totalReturnAmount 총 반품 금액
     * @param settlementAmount 실제 지급될 정산 금액
     * @param completedAt 정산 지급 완료 시간
     */
    public void completeSettlement(BigDecimal totalSalesAmount, BigDecimal totalCommissionAmount, BigDecimal totalReturnAmount, BigDecimal settlementAmount, LocalDateTime completedAt) {
        this.totalAmount = totalSalesAmount;
        this.commissionAmount = totalCommissionAmount;
        this.totalReturnAmount = totalReturnAmount;
        this.settlementAmount = settlementAmount;
        this.status = SettlementStatus.PAID; // Assuming PAID is the final status
        this.completedAt = completedAt;
    }
}
