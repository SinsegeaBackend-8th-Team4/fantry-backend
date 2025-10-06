package com.eneifour.fantry.checklist.controller;

import com.eneifour.fantry.checklist.dto.ChecklistItemDto;
import com.eneifour.fantry.checklist.service.ChecklistService;
import com.eneifour.fantry.checklist.service.PricingService;
import com.eneifour.fantry.inspection.support.api.InspectionApiResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/checklists")
@RequiredArgsConstructor
public class ChecklistController {
    private final ChecklistService checklistService;
    private final PricingService pricingService;

    // 카테고리별 체크리스트 목록 조회
    @GetMapping
    public InspectionApiResponse<List<ChecklistItemDto>> getChecklistsByCategory(
            @RequestParam @NotNull @Positive int goodsCategoryId) {
        List<ChecklistItemDto> categories = checklistService.getItemsByCategory(goodsCategoryId);
        return InspectionApiResponse.ok(categories);
    }

    // 카테고리별 최신 기준가 조회
    @GetMapping("/pricing/baseline")
    public InspectionApiResponse<Double> getPriceBaselineByCategoryId(
            @RequestParam @NotNull @Positive int goodsCategoryId) {
        Double baseline = pricingService.getLatestBaselineAmount(goodsCategoryId);
        return InspectionApiResponse.ok(baseline);
    }

    // 예상가 계산
    @PostMapping("/pricing/estimate")
    public InspectionApiResponse<Double> estimate(
            @RequestParam @NotNull @Positive int goodsCategoryId,
            @RequestBody(required = false) Map<String, String> selections) {
        if (selections == null) selections = Map.of();
        double estimate = pricingService.estimate(goodsCategoryId, selections);

        return InspectionApiResponse.ok(estimate);
    }
}
