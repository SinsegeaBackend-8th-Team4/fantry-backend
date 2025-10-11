package com.eneifour.fantry.checklist.repository;

import com.eneifour.fantry.checklist.domain.ChecklistTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChecklistTemplateRepository extends JpaRepository<ChecklistTemplate, Integer> {
    // 카테고리별 최신 템플릿 조회
    @Query("""
        select t from ChecklistTemplate t
        join ChecklistItemCategoryMap m on m.checklistItem.checklistTemplate = t
        where m.goodsCategory.goodsCategoryId = :categoryId
        and t.role = :role
        and t.status = :status
        order by t.version desc
    """)
    Optional<ChecklistTemplate> findLatestByCategoryAndRole(
            @Param("categoryId") int categoryId,
            @Param("role") ChecklistTemplate.Role role,
            @Param("status") ChecklistTemplate.Status status
    );
}
