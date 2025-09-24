package com.eneifour.fantry.checklist.dto;

import com.eneifour.fantry.catalog.dto.GoodsCategoryDto;
import com.eneifour.fantry.checklist.domain.PricingRule;

public record PricingRuleDto(
        int pricingRuleId,
        GoodsCategoryDto goodsCategory,
        String itemKey,
        String optionValue,
        PricingRule.EffectiveType effectiveType,
        double pctValue,
        double absValue,
        double capMinMultiplier,
        boolean active
        ) {
    public static PricingRuleDto from(PricingRule p) {
        return new PricingRuleDto(
                p.getPricingRuleId(),
                GoodsCategoryDto.from(p.getGoodsCategory()),
                p.getItemKey(),
                p.getOptionValue(),
                p.getEffectiveType(),
                p.getPctValue(),
                p.getAbsValue(),
                p.getCapMinMultiplier(),
                Boolean.TRUE.equals(p.getActive())
        );
    }
}
