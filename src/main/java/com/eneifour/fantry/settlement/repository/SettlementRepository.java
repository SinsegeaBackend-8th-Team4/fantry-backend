package com.eneifour.fantry.settlement.repository;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.settlement.domain.Settlement;
import com.eneifour.fantry.settlement.domain.SettlementStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Settlement (정산) 엔티티에 대한 데이터 액세스를 처리하는 리포지토리.
 */
@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Integer>, JpaSpecificationExecutor<Settlement> {
    Page<Settlement> findByMember(Member member, Pageable pageable);

    @Query("SELECT COALESCE(SUM(s.settlementAmount), 0) FROM Settlement s WHERE s.status = :status AND s.requestedAt BETWEEN :start AND :end")
    BigDecimal sumAmountByStatusAndRequestedAtBetween(@Param("status") SettlementStatus status, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(s.settlementAmount), 0) FROM Settlement s WHERE s.status = :status AND s.completedAt BETWEEN :start AND :end")
    BigDecimal sumAmountByStatusAndCompletedAtBetween(@Param("status") SettlementStatus status, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT count(s) FROM Settlement s WHERE s.status IN :statuses")
    long countByStatusIn(@Param("statuses") List<SettlementStatus> statuses);

    @Query("SELECT COALESCE(SUM(s.settlementAmount), 0) FROM Settlement s WHERE s.status = :status")
    BigDecimal sumAmountByStatus(@Param("status") SettlementStatus status);

    @Query("SELECT new map(" +
            "   count(s) as totalSettlements, " +
            "   COALESCE(sum(case when s.status = com.eneifour.fantry.settlement.domain.SettlementStatus.PENDING then 1 else 0 end), 0L) as pendingSettlements, " +
            "   COALESCE(sum(case when s.status = com.eneifour.fantry.settlement.domain.SettlementStatus.PAID then 1 else 0 end), 0L) as paidSettlements, " +
            "   COALESCE(sum(case when s.status = com.eneifour.fantry.settlement.domain.SettlementStatus.CANCELLED then 1 else 0 end), 0L) as cancelledSettlements, " +
            "   COALESCE(sum(case when s.status = com.eneifour.fantry.settlement.domain.SettlementStatus.FAILED then 1 else 0 end), 0L) as failedSettlements) " +
            "FROM Settlement s")
    Map<String, Long> countSettlementsByStatus();
}
