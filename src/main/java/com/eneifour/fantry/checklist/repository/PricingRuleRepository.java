package com.eneifour.fantry.checklist.repository;

import com.eneifour.fantry.checklist.domain.PricingRule;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface PricingRuleRepository extends JpaRepository<PricingRule, Integer> {
    @Query("""
    select r from PricingRule r
    where r.active = true
    and (r.goodsCategory is null or r.goodsCategory.goodsCategoryId = :goodsCategoryId)
    and r.itemKey in :itemKeys
    """)
    List<PricingRule> findActiveForEstimate(@Param("goodsCategoryId") int goodsCategoryId,  @Param("itemKeys") Collection<String> itemKeys);
}
