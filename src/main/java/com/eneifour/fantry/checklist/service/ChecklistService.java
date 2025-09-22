package com.eneifour.fantry.checklist.service;

import com.eneifour.fantry.catalog.domain.GoodsCategory;
import com.eneifour.fantry.checklist.dto.ChecklistItemDto;
import com.eneifour.fantry.checklist.repository.ChecklistItemCategoryMapRepository;
import com.eneifour.fantry.checklist.repository.ChecklistItemRepository;
import com.eneifour.fantry.checklist.repository.ChecklistTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.web.mappings.MappingsEndpoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChecklistService {
    private final ChecklistTemplateRepository checklistTemplateRepository;
    private final ChecklistItemRepository checklistItemRepository;
    private final ChecklistItemCategoryMapRepository checklistItemCategoryMapRepository;
    private final MappingsEndpoint mappingsEndpoint;

    /**
     * 체크리스트 항목 전체
     * @return
     */
    public List<ChecklistItemDto> getItems() {
        return checklistItemRepository.findAll()
                .stream()
                .map(ChecklistItemDto::from)
                .toList()
                ;
    }

    /**
     * 카테고리 별 체크리스트 항목 리스트
     * @param categoryId
     * @return
     */
    public List<ChecklistItemDto> getItemsByCategory(int categoryId){
        GoodsCategory goodsCategory = new GoodsCategory();
        goodsCategory.setGoodsCategoryId(categoryId);

        return checklistItemCategoryMapRepository.findByGoodsCategory(goodsCategory).stream()
                .map(map -> ChecklistItemDto.from(map.getChecklistItem()))
                .toList();
    }
}
