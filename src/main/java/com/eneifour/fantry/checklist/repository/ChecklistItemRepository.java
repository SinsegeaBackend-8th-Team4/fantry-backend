package com.eneifour.fantry.checklist.repository;

import com.eneifour.fantry.checklist.domain.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Integer> {
    // 체크리스트 템플릿 조회
    List<ChecklistItem> findAllByChecklistTemplate_ChecklistTemplateId(int checklistTemplateId);

    // 카테고리별 아이템 목록 조회
    @Query("""
        select i
        from ChecklistItem i
        join ChecklistItemCategoryMap m on m.checklistItem = i
        where m.goodsCategory.goodsCategoryId = :categoryId
        order by i.orderIndex asc
    """)
    List<ChecklistItem> findByCategoryId(int categoryId);

    Optional<ChecklistItem> findByItemKey(String itemKey);
}
