package com.eneifour.fantry.checklist.repository;

import com.eneifour.fantry.catalog.domain.GoodsCategory;
import com.eneifour.fantry.checklist.domain.ChecklistItemCategoryMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChecklistItemCategoryMapRepository extends JpaRepository<ChecklistItemCategoryMap, ChecklistItemCategoryMap.Id> {


    List<ChecklistItemCategoryMap> findByGoodsCategory (GoodsCategory goodsCategory);
}
