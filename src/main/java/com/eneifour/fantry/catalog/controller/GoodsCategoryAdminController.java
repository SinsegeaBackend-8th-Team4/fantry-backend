package com.eneifour.fantry.catalog.controller;

import com.eneifour.fantry.catalog.dto.GoodsCategoryCreateRequest;
import com.eneifour.fantry.catalog.dto.GoodsCategoryResponse;
import com.eneifour.fantry.catalog.dto.GoodsCategoryUpdateRequest;
import com.eneifour.fantry.catalog.service.GoodsCategoryService;
import com.eneifour.fantry.inspection.support.api.InspectionApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/catalog/goodsCategories")
public class GoodsCategoryAdminController {
    private final GoodsCategoryService goodsCategoryService;

    /**
     * 신규 굿즈 카테고리 등록
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201 Created 상태 코드 반환
    public InspectionApiResponse<GoodsCategoryResponse> createGoodsCategory(@Valid @RequestBody GoodsCategoryCreateRequest request) {
        GoodsCategoryResponse goodsCategory = goodsCategoryService.createGoodsCategory(request);
        return InspectionApiResponse.ok(goodsCategory);
    }

    /**
     * 기존 굿즈 카테고리 수정
     */
    @PutMapping("/{goodsCategoryId}")
    public InspectionApiResponse<GoodsCategoryResponse> updateGoodsCategory(
            @PathVariable int goodsCategoryId,
            @Valid @RequestBody GoodsCategoryUpdateRequest request ) {
        GoodsCategoryResponse goodsCategory = goodsCategoryService.updateGoodsCategory(goodsCategoryId, request);
        return InspectionApiResponse.ok(goodsCategory);
    }

    /**
     * 기존 굿즈 카테고리 삭제
     */
    @DeleteMapping("/{goodsCategoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public InspectionApiResponse<Void> deleteGoodsCategory(@PathVariable int goodsCategoryId) {
        goodsCategoryService.deleteGoodsCategory(goodsCategoryId);
        return InspectionApiResponse.ok(null);
    }
}
