package com.eneifour.fantry.checklist.controller;

import com.eneifour.fantry.checklist.service.PriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/price")
public class PriceController {
    private final PriceService priceService;

    // 카테고리별 최신 기준가 조회
    @GetMapping
    public Double getPriceBaselineByCategoryId(@RequestParam int goodsCategoryId) {
        return priceService.getLatestBaselineAmount(goodsCategoryId);
    }
}
