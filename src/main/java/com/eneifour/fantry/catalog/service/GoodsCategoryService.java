package com.eneifour.fantry.catalog.service;

import com.eneifour.fantry.catalog.domain.GoodsCategory;
import com.eneifour.fantry.catalog.dto.GoodsCategoryCreateRequest;
import com.eneifour.fantry.catalog.dto.GoodsCategoryResponse;
import com.eneifour.fantry.catalog.dto.GoodsCategoryUpdateRequest;
import com.eneifour.fantry.catalog.exception.CatalogErrorCode;
import com.eneifour.fantry.catalog.repository.GoodsCategoryRepository;
import com.eneifour.fantry.inspection.support.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 굿즈 카테고리 정보와 관련된 비즈니스 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoodsCategoryService {
    private final GoodsCategoryRepository goodsCategoryRepository;

    /**
     * 모든 굿즈 카테고리 목록을 조회
     * @return 모든 굿즈 카테고리 DTO 리스트
     */
    public List<GoodsCategoryResponse> getAllCategories() {
        return goodsCategoryRepository.findAll()
                .stream()
                .map(GoodsCategoryResponse::from)
                .toList();
    }

    /**
     * 신규 굿즈 카테고리 등록
     * @param request 등록할 카테고리 정보
     * @return 생성된 카테고리 정보
     */
    @Transactional
    public GoodsCategoryResponse createGoodsCategory(GoodsCategoryCreateRequest request) {
        GoodsCategory savedCategory = goodsCategoryRepository.save(request.toEntity());

        return GoodsCategoryResponse.from(savedCategory);
    }

    /**
     * 기존 굿즈 카테고리 수정
     * @param goodsCategoryId 수정할 카테고리 ID
     * @param request 수정할 카테고리 정보
     * @return 수정된 카테고리 정보
     */
    @Transactional
    public GoodsCategoryResponse updateGoodsCategory(int goodsCategoryId, GoodsCategoryUpdateRequest request) {
        GoodsCategory goodsCategory = goodsCategoryRepository.findById(goodsCategoryId).orElseThrow(
                () -> new BusinessException(CatalogErrorCode.CATEGORY_NOT_FOUND));

        goodsCategory.setCode(request.getCode());
        goodsCategory.setName(request.getName());

        return GoodsCategoryResponse.from(goodsCategory);
    }

    /**
     * 굿즈 카테고리 삭제
     * @param goodsCategoryId 삭제할 카테고리 ID
     */
    @Transactional
    public void deleteGoodsCategory(int goodsCategoryId) {
        if(!goodsCategoryRepository.existsById(goodsCategoryId)) throw new BusinessException(CatalogErrorCode.CATEGORY_NOT_FOUND);

        goodsCategoryRepository.deleteById(goodsCategoryId);
    }
}
