package com.eneifour.fantry.checklist.service;

import com.eneifour.fantry.checklist.domain.PricingRule;
import com.eneifour.fantry.checklist.repository.PriceBaselineRepository;
import com.eneifour.fantry.checklist.repository.PricingRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PricingService {
    private final PriceBaselineRepository priceBaselineRepository;
    private final PricingRuleRepository pricingRuleRepository;

    // 카테고리별 최신 기준가 조회
    public Double getLatestBaselineAmount(int goodsCategoryId) {
        return priceBaselineRepository.findTopAmount(goodsCategoryId, LocalDateTime.now()).orElse(null);
    }

    // 예상가 계산
    public double estimate(int goodsCategoryId, Map<String, String> selections) {
        Double baselineObj = getLatestBaselineAmount(goodsCategoryId);
        log.debug("baselineObj: {}", baselineObj);
        // 기준가 없으면 0 반환
        if(baselineObj == null) return 0.0;
        double baseline = baselineObj;

        // 룰 조회
        List<PricingRule> rules = pricingRuleRepository.findActiveForEstimate(goodsCategoryId, selections.keySet());

        // 사용자가 고른 값과 일치하는 룰 적용
        List<PricingRule> applied = rules.stream()
                .filter(r -> {
                    String v = selections.get(r.getItemKey()); // 사용자 답변
                    return v != null && v.equals(r.getOptionValue());
                }).toList();

        // 퍼센트 적용
        double pctSum = applied.stream()
                .filter(r->r.getEffectiveType() == PricingRule.EffectiveType.PCT)
                .mapToDouble(PricingRule::getPctValue)
                .sum();

        double absSum = applied.stream()
                .filter(r -> r.getEffectiveType() == PricingRule.EffectiveType.ABS)
                .mapToDouble(PricingRule::getAbsValue)
                .sum();

        double capMul = applied.stream()
                .filter(r -> r.getEffectiveType() == PricingRule.EffectiveType.CAP)
                .mapToDouble(PricingRule::getCapMinMultiplier)
                .max()
                .orElse(0.0);

        double price = baseline * (1.0 + pctSum) + absSum;

        // CAP 있으면 최저가 보정
        if (capMul > 0.0) {
            double floor = baseline * capMul;
            if (price < floor) price = floor;
        }

        return price;
    }
}
