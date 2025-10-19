package com.eneifour.fantry.checklist.service;

import com.eneifour.fantry.catalog.exception.CatalogErrorCode;
import com.eneifour.fantry.catalog.repository.ArtistRepository;
import com.eneifour.fantry.catalog.repository.GoodsCategoryRepository;
import com.eneifour.fantry.checklist.domain.PricingRule;
import com.eneifour.fantry.checklist.dto.MarketAvgPriceResponse;
import com.eneifour.fantry.checklist.exception.ChecklistErrorCode;
import com.eneifour.fantry.checklist.repository.PriceBaselineRepository;
import com.eneifour.fantry.checklist.repository.PricingRuleRepository;
import com.eneifour.fantry.inspection.domain.InspectionStatus;
import com.eneifour.fantry.inspection.repository.InspectionRepository;
import com.eneifour.fantry.inspection.support.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 가격(기준가, 예상가, 평균가) 관련 비즈니스 로직을 처리하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PricingService {
    private final PriceBaselineRepository priceBaselineRepository;
    private final PricingRuleRepository pricingRuleRepository;
    private final GoodsCategoryRepository goodsCategoryRepository;
    private final ArtistRepository artistRepository;
    private final InspectionRepository inspectionRepository;

    /**
     * 특정 카테고리의 가장 최신 기준가 조회
     * @param goodsCategoryId 조회할 굿즈 카테고리의 ID
     * @return 최신 기준가
     * @throws BusinessException 요청한 카테고리가 없거나, 기준가가 없을 경우 발생
     */
    public Double getLatestBaselineAmount(int goodsCategoryId) {
        // 카테고리 존재 여부 검증
        goodsCategoryRepository.findById(goodsCategoryId).orElseThrow(()->new BusinessException(CatalogErrorCode.CATEGORY_NOT_FOUND));
        // 최신 기준가 조회 및 예외 처리
        return priceBaselineRepository.findTopAmount(goodsCategoryId, LocalDateTime.now())
                .orElseThrow(()->new BusinessException(ChecklistErrorCode.BASELINE_NOT_FOUND));
    }

    /**
     * 사용자 체크리스트 선택지 바탕으로 상품의 예상가 계산
     * @param goodsCategoryId 예상가를 계산할 굿즈 카테고리 ID
     * @param selections 사용자가 선택한 체크리스트 항목들
     * @return 계산된 예상가
     */
    public double estimate(int goodsCategoryId, Map<String, String> selections) {
        // 카테고리 별 최신 기준가 조회
        double baseline = priceBaselineRepository.findTopAmount(goodsCategoryId, LocalDateTime.now()).orElse(0.0); // 기준가 없으면 0 반환
        log.debug("카테고리 '{}'의 기준가: {}", goodsCategoryId, baseline);
        // 기준가 없으면 0 반환
        if(baseline == 0.0) return 0.0;

        // 적용 가능한 가격 정책 조회
        List<PricingRule> rules = pricingRuleRepository.findActiveForEstimate(goodsCategoryId, selections.keySet());

        // 사용자가 고른 값과 일치하는 정책 적용
        List<PricingRule> appliedRule = rules.stream()
                .filter(rule -> {
                    String userAnswer = selections.get(rule.getItemKey()); // 사용자 답변
                    return userAnswer != null && userAnswer.equals(rule.getOptionValue());
                }).toList();

        // 정책 유형별 값 계산
        // 퍼센트 타입
        double pctSum = appliedRule.stream()
                .filter(r->r.getEffectiveType() == PricingRule.EffectiveType.PCT)
                .mapToDouble(PricingRule::getPctValue)
                .sum();

        // 절대값 타입
        double absSum = appliedRule.stream()
                .filter(r -> r.getEffectiveType() == PricingRule.EffectiveType.ABS)
                .mapToDouble(PricingRule::getAbsValue)
                .sum();

        // 최저가 보장 타입
        double capMul = appliedRule.stream()
                .filter(r -> r.getEffectiveType() == PricingRule.EffectiveType.CAP)
                .mapToDouble(PricingRule::getCapMinMultiplier)
                .max()
                .orElse(0.0);

        // 최종 예상가 계산 (기준가 * (1 + 퍼센트 합)) + 절대값 합
        double estimatedPrice = baseline * (1.0 + pctSum) + absSum;

        // CAP 있으면 최저가 보정
        if (capMul > 0.0) {
            double floor = baseline * capMul;
            if (estimatedPrice < floor) estimatedPrice = floor;
        }

        log.debug("최종 계산된 예상가: {}", estimatedPrice);
        return estimatedPrice;
    }

    /**
     * 조건에 맞는 상품의 평균 시세 조회
     * @param goodsCategoryId 카테고리 ID
     * @param artistId 아티스트 ID
     * @param albumId 앨범 ID (null 가능)
     * @return MarketAvgPriceResponse
     */
    public MarketAvgPriceResponse getMarketAvgPrice(int goodsCategoryId, int artistId, Integer albumId) {
        // 카테고리, 아티스트 존재 여부 검증
        goodsCategoryRepository.findById(goodsCategoryId).orElseThrow(()->new BusinessException(CatalogErrorCode.CATEGORY_NOT_FOUND));
        artistRepository.findById(artistId).orElseThrow(()->new BusinessException(CatalogErrorCode.ARTIST_NOT_FOUND));

        BigDecimal marketAvgPrice = inspectionRepository.getMarketAvgPrice(goodsCategoryId, artistId, albumId, InspectionStatus.COMPLETED).orElse(null);
        int count = inspectionRepository.countForMarketPrice(goodsCategoryId, artistId, albumId, InspectionStatus.COMPLETED);

        if (marketAvgPrice != null) {
            marketAvgPrice = marketAvgPrice.setScale(0, RoundingMode.HALF_UP);
        }

        return new MarketAvgPriceResponse(marketAvgPrice, count);
    }
}