package com.eneifour.fantry.catalog.controller;

import com.eneifour.fantry.catalog.dto.GoodsCategoryDto;
import com.eneifour.fantry.catalog.service.GoodsCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/goodsCategory")
@RequiredArgsConstructor
public class GoodsCategoryController {
    private final GoodsCategoryService goodsCategoryService;

    // 굿즈 카테고리 전체 조회
    public List<GoodsCategoryDto> getGoodsCategories() {
        return goodsCategoryService.getAllGategories();
    }
}
