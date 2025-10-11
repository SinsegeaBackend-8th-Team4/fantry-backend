package com.eneifour.fantry.checklist.controller;

import com.eneifour.fantry.checklist.dto.MarketAvgPriceResponse;
import com.eneifour.fantry.checklist.service.PricingService;
import com.eneifour.fantry.inspection.support.api.InspectionApiResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/pricing")
@RequiredArgsConstructor
public class PricingController {
    private final PricingService pricingService;

    // 카테고리별 최신 기준가 조회
    @GetMapping("/baseline")
    public InspectionApiResponse<Double> getPriceBaselineByCategoryId(
            @RequestParam @NotNull @Positive int goodsCategoryId) {
        Double baseline = pricingService.getLatestBaselineAmount(goodsCategoryId);
        return InspectionApiResponse.ok(baseline);
    }

    // 예상가 계산
    @PostMapping("/estimate")
    public InspectionApiResponse<Double> estimate(
            @RequestParam @NotNull @Positive int goodsCategoryId,
            @RequestBody(required = false) Map<String, String> selections) {
        if (selections == null) selections = Map.of();
        double estimate = pricingService.estimate(goodsCategoryId, selections);

        return InspectionApiResponse.ok(estimate);
    }

    // 평균 시세 조회
    @GetMapping("/marketAverage")
    public InspectionApiResponse<MarketAvgPriceResponse> getMarketAveragePrice(
            @RequestParam @NotNull @Positive int goodsCategoryId,
            @RequestParam @NotNull @Positive int artistId,
            @RequestParam(required = false) Integer albumId) {
        MarketAvgPriceResponse response = pricingService.getMarketAvgPrice(goodsCategoryId, artistId, albumId);

        return InspectionApiResponse.ok(response);
    }
}
