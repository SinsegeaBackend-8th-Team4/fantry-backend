package com.eneifour.fantry.checklist.controller;

import com.eneifour.fantry.checklist.dto.PriceBaselineDto;
import com.eneifour.fantry.checklist.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/price")
public class PriceController {
    private final PriceService priceService;

    @GetMapping
    public List<PriceBaselineDto> getPriceBaselineByCategoryId(@RequestParam int goodsCategoryId) {
        return priceService.findWithCategoryByCategoryId(goodsCategoryId);
    }
}
