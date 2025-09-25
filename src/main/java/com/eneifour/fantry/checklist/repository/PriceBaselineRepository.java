package com.eneifour.fantry.checklist.repository;

import com.eneifour.fantry.checklist.domain.PriceBaseline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PriceBaselineRepository extends JpaRepository<PriceBaseline, Integer> {
    // 카테고리별 최신 기준가 조회
    @Query("""
        SELECT pb.amount FROM PriceBaseline pb
        WHERE pb.goodsCategory.goodsCategoryId = :goodsCategoryId
        AND pb.effectiveAt <= :now
        ORDER BY pb.effectiveAt DESC, pb.priceBaselineId DESC
    """)
    Optional<Double> findTopAmount(@Param("goodsCategoryId") int goodsCategoryId, @Param("now") LocalDateTime now);
}
