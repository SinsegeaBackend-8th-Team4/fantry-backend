package com.eneifour.fantry.catalog.dto;

import com.eneifour.fantry.catalog.domain.GoodsCategory;

public record GoodsCategoryDto(Integer GoodsCategoryId, String code, String name) {
    public static GoodsCategoryDto from(GoodsCategory g) {
        return new GoodsCategoryDto(
                g.getGoodsCategoryId(),
                g.getCode(),
                g.getName()
        );
    }
}
