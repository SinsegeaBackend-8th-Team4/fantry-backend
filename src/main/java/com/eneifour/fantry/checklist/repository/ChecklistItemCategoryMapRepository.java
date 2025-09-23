package com.eneifour.fantry.checklist.repository;

import com.eneifour.fantry.checklist.domain.ChecklistItemCategoryMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistItemCategoryMapRepository extends JpaRepository<ChecklistItemCategoryMap, ChecklistItemCategoryMap.Id> {
}
