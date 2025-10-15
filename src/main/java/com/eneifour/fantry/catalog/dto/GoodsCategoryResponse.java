package com.eneifour.fantry.catalog.dto;

import com.eneifour.fantry.catalog.domain.GoodsCategory;

import java.time.LocalDateTime;

public record GoodsCategoryResponse(int GoodsCategoryId, String code, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static GoodsCategoryResponse from(GoodsCategory g) {
        return new GoodsCategoryResponse(
                g.getGoodsCategoryId(),
                g.getCode(),
                g.getName(),
                g.getCreatedAt(),
                g.getUpdatedAt()
        );
    }
}
