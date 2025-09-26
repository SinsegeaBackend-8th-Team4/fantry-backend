package com.eneifour.fantry.checklist.dto;

import com.eneifour.fantry.catalog.dto.GoodsCategoryDto;
import com.eneifour.fantry.checklist.domain.PriceBaseline;

public record PriceBaselineDto(int priceBaselineId, GoodsCategoryDto goodsCategory, PriceBaseline.Source source, double amount) {
    public static PriceBaselineDto from(PriceBaseline p) {
        return new PriceBaselineDto(
                p.getPriceBaselineId(),
                GoodsCategoryDto.from(p.getGoodsCategory()),
                p.getSource(),
                p.getAmount()
        );
    }
}
