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
 * CommissionRule (수수료 규칙)
 * 카테고리, 사용자 등급 등 특정 조건에 따라 다른 수수료율을 적용하기 위한 엔티티.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "commission_rule")
public class CommissionRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ruleId;

    @Column(nullable = false)
    private String ruleName;

    @Column(nullable = false)
    private BigDecimal commissionRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConditionType conditionType;

    @Column(nullable = false)
    private String conditionValue;

    @Column(nullable = false)
    private int priority;

    @Column(nullable = false)
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Column(nullable = false)
    private boolean isActive;

    @CreatedDate
    @Column(updatable = false, nullable = false)
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
}
