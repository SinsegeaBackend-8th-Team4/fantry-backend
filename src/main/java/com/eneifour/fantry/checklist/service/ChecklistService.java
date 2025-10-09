package com.eneifour.fantry.checklist.service;

import com.eneifour.fantry.catalog.exception.CatalogErrorCode;
import com.eneifour.fantry.catalog.repository.GoodsCategoryRepository;
import com.eneifour.fantry.checklist.dto.ChecklistItemResponse;
import com.eneifour.fantry.checklist.repository.ChecklistItemRepository;
import com.eneifour.fantry.inspection.support.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 체크리스트 관련 비즈니스 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChecklistService {
    private final ChecklistItemRepository itemRepository;
    private final GoodsCategoryRepository goodsCategoryRepository;

    /**
     * 특정 카테고리에 속한 모든 체크리스트 항목 조회
     * @param categoryId 조회할 굿즈 카테고리의 ID
     * @return 해당 카테고리에 대한 ChecklistItemResponse 리스트
     * @throws BusinessException 요청한 카테고리가 존재하지 않을 경우 발생
     */
    public List<ChecklistItemResponse> getItemsByCategory(int categoryId){
        // 카테고리 존재 검증
        goodsCategoryRepository.findById(categoryId).orElseThrow(()->new BusinessException(CatalogErrorCode.CATEGORY_NOT_FOUND));

        return itemRepository.findByCategoryId(categoryId).stream()
                .map(ChecklistItemResponse::from)
                .toList();
    }
}
