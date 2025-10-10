package com.eneifour.fantry.catalog.dto;

import com.eneifour.fantry.catalog.domain.GoodsCategory;

public record GoodsCategoryResponse(int GoodsCategoryId, String code, String name) {
    public static GoodsCategoryResponse from(GoodsCategory g) {
        return new GoodsCategoryResponse(
                g.getGoodsCategoryId(),
                g.getCode(),
                g.getName()
        );
    }
}
