package com.eneifour.fantry.settlement.repository;

import com.eneifour.fantry.settlement.domain.SettlementSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * SettlementSetting (정산 설정) 엔티티에 대한 데이터 액세스를 처리하는 리포지토리.
 */
@Repository
public interface SettlementSettingRepository extends JpaRepository<SettlementSetting, Integer> {

    /**
     * 가장 최근에 생성된 정산 설정을 조회합니다.
     * @return Optional<SettlementSetting>
     */
    Optional<SettlementSetting> findFirstByOrderByCreatedAtDesc();
}