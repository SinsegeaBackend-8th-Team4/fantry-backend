package com.eneifour.fantry.settlement.service;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.settlement.domain.Settlement;
import com.eneifour.fantry.settlement.dto.SettlementDetailResponse;
import com.eneifour.fantry.settlement.dto.SettlementSummaryResponse;
import com.eneifour.fantry.settlement.exception.SettlementErrorCode;
import com.eneifour.fantry.settlement.exception.SettlementException;
import com.eneifour.fantry.settlement.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 판매자용 정산 관련 서비스를 담당합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementService {

    private final SettlementRepository settlementRepository;

    /**
     * 현재 로그인한 판매자의 정산 내역 목록을 조회합니다.
     * @param seller   현재 판매자
     * @param pageable 페이징 정보
     * @return Page<SettlementSummaryResponse>
     */
    public Page<SettlementSummaryResponse> getMySettlements(Member seller, Pageable pageable) {
        // TODO: 추후 Specification을 사용하여 판매자 ID로 검색하도록 개선할 수 있음
        return settlementRepository.findByMember(seller, pageable)
                .map(SettlementSummaryResponse::from);
    }

    /**
     * 현재 로그인한 판매자의 특정 정산 건 상세 내역을 조회합니다.
     * @param seller       현재 판매자
     * @param settlementId 정산 ID
     * @return SettlementDetailResponse
     */
    public SettlementDetailResponse getMySettlementDetail(Member seller, int settlementId) {
        Settlement settlement = settlementRepository.findById(settlementId)
                .orElseThrow(() -> new SettlementException(SettlementErrorCode.SETTLEMENT_NOT_FOUND));

        // 본인의 정산 내역이 맞는지 확인
        if (settlement.getMember().getMemberId() != seller.getMemberId()) {
            throw new SettlementException(SettlementErrorCode.FORBIDDEN_ACCESS); // FORBIDDEN_ACCESS 에러 코드 추가 필요
        }

        return SettlementDetailResponse.from(settlement);
    }
}
