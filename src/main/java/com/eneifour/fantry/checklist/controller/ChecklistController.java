package com.eneifour.fantry.checklist.controller;

import com.eneifour.fantry.checklist.dto.ChecklistItemDto;
import com.eneifour.fantry.checklist.service.ChecklistService;
import com.eneifour.fantry.checklist.service.PricingService;
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
    public List<ChecklistItemDto> getChecklistsByCategory(@RequestParam int goodsCategoryId) {
        return checklistService.getItemsByCategory(goodsCategoryId);
    }

    // 카테고리별 최신 기준가 조회
    @GetMapping("/pricing/baseline")
    public Double getPriceBaselineByCategoryId(@RequestParam int goodsCategoryId) {
        return pricingService.getLatestBaselineAmount(goodsCategoryId);
    }

    // 예상가 계산
    @PostMapping("/pricing/estimate")
    public double estimate(@RequestParam int goodsCategoryId, @RequestBody(required = false) Map<String, String> selections) {
        if (selections == null) selections = Map.of();
        return pricingService.estimate(goodsCategoryId, selections);
    }
}
