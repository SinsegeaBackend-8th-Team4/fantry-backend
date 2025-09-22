package com.eneifour.fantry.catalog.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "goods_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int goodsCategoryId;

    private String code;
    private String name;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
