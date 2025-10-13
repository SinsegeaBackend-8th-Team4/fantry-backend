package com.eneifour.fantry.settlement.repository;

import com.eneifour.fantry.settlement.domain.CommissionRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CommissionRule (수수료 규칙) 엔티티에 대한 데이터 액세스를 처리하는 리포지토리.
 */
@Repository
public interface CommissionRuleRepository extends JpaRepository<CommissionRule, Integer> {
}
