package com.eneifour.fantry.checklist.domain;

import com.eneifour.fantry.catalog.domain.GoodsCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pricing_rule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PricingRule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pricingRuleId;

    @ManyToOne
    @JoinColumn(name = "goods_category_id")
    private GoodsCategory goodsCategory;

    private String itemKey;
    private String optionValue;

    @Enumerated(EnumType.STRING)
    private EffectiveType effectiveType;

    private double pctValue;
    private double absValue;
    private double capMinMultiplier;
    private Boolean active;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public enum EffectiveType { PCT, ABS, CAP }
}