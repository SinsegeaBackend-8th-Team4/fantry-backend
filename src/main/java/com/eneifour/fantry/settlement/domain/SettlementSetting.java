package com.eneifour.fantry.settlement.domain;

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

/**
 * SettlementSetting (정산 설정)
 * 정산 주기, 수수료율 등 정산 시스템의 기본 정책을 관리하는 엔티티.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "settlement_setting")
public class SettlementSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int settlementSettingId;

    @Column(nullable = false)
    private BigDecimal commissionRate; // 기본 수수료율

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SettlementCycleType settlementCycleType; // 정산 주기 유형

    private Integer settlementDay; // 정산 주기 상세일 (주: 1-7, 월: 1-31)

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
     * 정산 설정을 업데이트하는 비즈니스 메서드.
     */
    public void update(BigDecimal commissionRate, SettlementCycleType settlementCycleType, Integer settlementDay, Member updatedBy) {
        this.commissionRate = commissionRate;
        this.settlementCycleType = settlementCycleType;
        this.settlementDay = settlementDay;
        this.updatedBy = updatedBy;
    }
}
