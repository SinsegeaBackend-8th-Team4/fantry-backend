package com.eneifour.fantry.settlement.repository;

import com.eneifour.fantry.settlement.domain.SettlementItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * SettlementItem (정산 항목) 엔티티에 대한 데이터 액세스를 처리하는 리포지토리.
 */
@Repository
public interface SettlementItemRepository extends JpaRepository<SettlementItem, Integer> {
}
