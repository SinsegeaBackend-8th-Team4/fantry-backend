package com.eneifour.fantry.catalog.controller;

import com.eneifour.fantry.catalog.dto.GoodsCategoryDto;
import com.eneifour.fantry.catalog.service.GoodsCategoryService;
import com.eneifour.fantry.inspection.support.api.InspectionApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/catalog/goodsCategories")
public class GoodsCategoryController {
    private final GoodsCategoryService goodsCategoryService;

    // 굿즈 카테고리 전체 조회
    @GetMapping
    public InspectionApiResponse<List<GoodsCategoryDto>> getGoodsCategories() {
        List<GoodsCategoryDto> categories = goodsCategoryService.getAllCategories();
        return InspectionApiResponse.ok(categories);
    }
}
