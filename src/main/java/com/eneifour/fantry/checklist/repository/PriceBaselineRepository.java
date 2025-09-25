package com.eneifour.fantry.checklist.repository;

import com.eneifour.fantry.checklist.domain.PriceBaseline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceBaselineRepository extends JpaRepository<PriceBaseline, Integer> {
    // 카테고리(fetch join)별 최신 기준가 조회
    @Query("""
        SELECT pb FROM PriceBaseline pb
        JOIN FETCH pb.goodsCategory gc
        WHERE gc.goodsCategoryId = :goodsCategoryId
        ORDER BY pb.effectiveAt DESC
    """)
    List<PriceBaseline> findLatestBaselineByCategoryId(@Param("goodsCategoryId") int goodsCategoryId);
}
