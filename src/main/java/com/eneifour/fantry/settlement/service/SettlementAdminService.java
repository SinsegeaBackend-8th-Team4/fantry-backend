package com.eneifour.fantry.settlement.service;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.settlement.domain.SettlementSetting;
import com.eneifour.fantry.settlement.dto.SettlementSettingRequest;
import com.eneifour.fantry.settlement.dto.SettlementSettingResponse;
import com.eneifour.fantry.settlement.exception.SettlementErrorCode;
import com.eneifour.fantry.settlement.exception.SettlementException;
import com.eneifour.fantry.settlement.repository.SettlementSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 관리자용 정산 관련 서비스를 담당합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SettlementAdminService {

    private final SettlementSettingRepository settlementSettingRepository;

    /**
     * 현재 적용 중인 정산 설정을 조회합니다.
     * @return SettlementSettingResponse
     */
    @Transactional(readOnly = true)
    public SettlementSettingResponse getSettlementSetting() {
        SettlementSetting setting = settlementSettingRepository.findFirstByOrderByCreatedAtDesc()
                .orElseThrow(() -> new SettlementException(SettlementErrorCode.SETTING_NOT_FOUND));
        return SettlementSettingResponse.from(setting);
    }

    /**
     * 정산 설정을 생성하거나 수정합니다.
     * 시스템에는 단 하나의 설정만 존재하므로, 기존 설정이 있으면 업데이트하고 없으면 새로 생성합니다.
     * @param request 설정 요청 DTO
     * @param admin   작업을 수행하는 관리자
     * @return SettlementSettingResponse
     */
    public SettlementSettingResponse createOrUpdateSettlementSetting(SettlementSettingRequest request, Member admin) {
        // 수수료율 유효성 검사는 DTO의 @DecimalMin/Max Annotation으로 처리되므로 서비스 로직에서 제거.

        // 기존 설정이 있는지 확인
        return settlementSettingRepository.findFirstByOrderByCreatedAtDesc()
                .map(existingSetting -> {
                    // 기존 설정이 있으면 업데이트
                    existingSetting.update(
                            request.commissionRate(),
                            request.settlementCycleType(),
                            request.settlementDay(),
                            admin
                    );
                    return SettlementSettingResponse.from(existingSetting);
                })
                .orElseGet(() -> {
                    // 기존 설정이 없으면 새로 생성
                    SettlementSetting newSetting = request.toEntity(admin);
                    SettlementSetting savedSetting = settlementSettingRepository.save(newSetting);
                    return SettlementSettingResponse.from(savedSetting);
                });
    }
}