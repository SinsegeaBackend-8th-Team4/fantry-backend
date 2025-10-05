package com.eneifour.fantry.checklist.repository;

import com.eneifour.fantry.checklist.domain.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Integer> {
    // 카테고리 -> 아이템 목록
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
