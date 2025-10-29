package com.eneifour.fantry.checklist.repository;

import com.eneifour.fantry.checklist.domain.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Integer> {
    /**
     * 템플릿에 속한 여러 카테고리의 모든 항목 반환
     * @param checklistTemplateId 템플릿 ID
     * @return 템플릿에 속한 모든 항목
     */
    List<ChecklistItem> findAllByChecklistTemplate_ChecklistTemplateId(int checklistTemplateId);

    /**
     * 특정 카테고리의 모든 항목 반환
     * @param categoryId 카테고리 ID
     * @return 카테고리에 속한 모든 항목
     */
    @Query("""
        select i
        from ChecklistItem i
        join ChecklistItemCategoryMap m on m.checklistItem = i
        where m.goodsCategory.goodsCategoryId = :categoryId
        order by i.orderIndex asc
    """)
    List<ChecklistItem> findByCategoryId(int categoryId);

    /**
     * 해당 템플릿에서 해당 카테고리 속한 모든 항목 반환
     * @param templateId 템플릿 ID
     * @param categoryId 카테고리 ID
     * @return 템플릿 + 카테고리 모든 항목
     */
    @Query("""
        select distinct i
        from ChecklistItem i
        join ChecklistItemCategoryMap m on m.checklistItem = i
        where i.checklistTemplate.checklistTemplateId = :templateId
        and m.goodsCategory.goodsCategoryId = :categoryId
        order by i.orderIndex asc
""")
    List<ChecklistItem> findByTemplateIdAndCategoryId(int templateId, int categoryId);

    /** 라벨 매핑용 */
    Optional<ChecklistItem> findByItemKey(String itemKey);
}
