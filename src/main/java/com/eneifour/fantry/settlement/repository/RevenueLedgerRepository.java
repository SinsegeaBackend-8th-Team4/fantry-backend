package com.eneifour.fantry.settlement.repository;

import com.eneifour.fantry.settlement.domain.RevenueLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * RevenueLedger (매출 장부) 엔티티에 대한 데이터 액세스를 처리하는 리포지토리.
 */
@Repository
public interface RevenueLedgerRepository extends JpaRepository<RevenueLedger, Long> {
}
