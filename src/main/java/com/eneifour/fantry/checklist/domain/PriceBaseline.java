package com.eneifour.fantry.checklist.domain;

import com.eneifour.fantry.catalog.domain.GoodsCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "price_baseline")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceBaseline {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int priceBaselineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_category_id")
    private GoodsCategory goodsCategory;

    @Enumerated(EnumType.STRING)
    private Source source;

    private double amount;
    private LocalDateTime effectiveAt;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;
    public enum Source { SEED, MSRP_BASE, MARKET_AVG }
}