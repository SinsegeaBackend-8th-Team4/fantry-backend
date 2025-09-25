package com.eneifour.fantry.checklist.controller;

import com.eneifour.fantry.checklist.service.PricingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pricing")
public class PricingController {
    private final PricingService pricingService;

    // 카테고리별 최신 기준가 조회
    @GetMapping("/baseline")
    public Double getPriceBaselineByCategoryId(@RequestParam int goodsCategoryId) {
        return pricingService.getLatestBaselineAmount(goodsCategoryId);
    }
}
