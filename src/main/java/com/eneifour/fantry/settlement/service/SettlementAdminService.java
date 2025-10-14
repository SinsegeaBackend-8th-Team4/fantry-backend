package com.eneifour.fantry.settlement.service;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.repository.MemberRepository;
import com.eneifour.fantry.orders.domain.Orders;
import com.eneifour.fantry.orders.domain.OrderStatus;
import com.eneifour.fantry.orders.repository.OrdersRepository;
import com.eneifour.fantry.settlement.domain.*;
import com.eneifour.fantry.settlement.dto.SettlementSettingRequest;
import com.eneifour.fantry.settlement.dto.SettlementSettingResponse;
import com.eneifour.fantry.settlement.exception.SettlementErrorCode;
import com.eneifour.fantry.settlement.exception.SettlementException;
import com.eneifour.fantry.settlement.repository.CommissionRuleRepository;
import com.eneifour.fantry.settlement.repository.RevenueLedgerRepository;
import com.eneifour.fantry.settlement.repository.SettlementRepository;
import com.eneifour.fantry.settlement.repository.SettlementSettingRepository;
import com.eneifour.fantry.settlement.dto.SettlementDashboardResponse;
import com.eneifour.fantry.settlement.dto.SettlementDetailResponse;
import com.eneifour.fantry.settlement.dto.SettlementSearchCondition;
import com.eneifour.fantry.settlement.dto.SettlementSummaryResponse;
import com.eneifour.fantry.settlement.repository.SettlementSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 관리자용 정산 관련 서비스를 담당합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SettlementAdminService {

    private final SettlementSettingRepository settlementSettingRepository;
    private final OrdersRepository ordersRepository;
    private final SettlementRepository settlementRepository;
    private final RevenueLedgerRepository revenueLedgerRepository;
    private final CommissionRuleRepository commissionRuleRepository;
    private final SettlementSpecification settlementSpecification;
    private final MemberRepository memberRepository;

    /**
     * 대시보드에 표시될 정산 KPI 요약 정보를 조회합니다.
     * @return SettlementDashboardResponse
     */
    @Transactional(readOnly = true)
    public SettlementDashboardResponse getDashboardSummary() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        YearMonth currentMonth = YearMonth.from(today);

        // 당월 정산 예정액
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(LocalTime.MAX);
        BigDecimal monthlyScheduledAmount = settlementRepository.sumAmountByStatusAndRequestedAtBetween(SettlementStatus.PENDING, startOfMonth, endOfMonth);

        // 어제 정산된 금액
        LocalDateTime startOfYesterday = yesterday.atStartOfDay();
        LocalDateTime endOfYesterday = yesterday.atTime(LocalTime.MAX);
        BigDecimal yesterdaySettledAmount = settlementRepository.sumAmountByStatusAndCompletedAtBetween(SettlementStatus.PAID, startOfYesterday, endOfYesterday);

        // 정산 보류/실패 건수
        long pendingOrFailedCount = settlementRepository.countByStatusIn(List.of(SettlementStatus.PENDING, SettlementStatus.FAILED));

        // 누적 정산액
        BigDecimal cumulativeSettlementAmount = settlementRepository.sumAmountByStatus(SettlementStatus.PAID);

        return SettlementDashboardResponse.builder()
                .monthlyScheduledAmount(monthlyScheduledAmount)
                .yesterdaySettledAmount(yesterdaySettledAmount)
                .pendingOrFailedCount(pendingOrFailedCount)
                .cumulativeSettlementAmount(cumulativeSettlementAmount)
                .build();
    }

    /**
     * 검색 조건에 맞는 정산 내역 목록을 페이징하여 조회합니다.
     * @param condition 검색 조건
     * @param pageable 페이징 정보
     * @return Page<SettlementSummaryResponse>
     */
    @Transactional(readOnly = true)
    public Page<SettlementSummaryResponse> getSettlements(SettlementSearchCondition condition, Pageable pageable) {
        return settlementRepository.findAll(settlementSpecification.toSpecification(condition), pageable)
                .map(SettlementSummaryResponse::from);
    }

    /**
     * 특정 정산 건의 상세 내역을 조회합니다.
     * @param settlementId 정산 ID
     * @return SettlementDetailResponse
     */
    @Transactional(readOnly = true)
    public SettlementDetailResponse getSettlementDetail(int settlementId) {
        Settlement settlement = settlementRepository.findById(settlementId)
                .orElseThrow(() -> new SettlementException(SettlementErrorCode.SETTLEMENT_NOT_FOUND)); // SETTLEMENT_NOT_FOUND 에러 코드 추가 필요
        return SettlementDetailResponse.from(settlement);
    }


    /**
     * 특정 날짜를 기준으로 정산을 실행합니다.
     * @param settlementDate 정산 기준일
     */
    public void executeSettlement(LocalDate settlementDate) {
        // 1. 정산 대상 기간 정의 (예: 정산일 기준 7일 이전까지)
        LocalDateTime cutoffDate = settlementDate.atStartOfDay().minusDays(7);

        // 2. 정산 대상 주문 조회 (배송 완료 상태, 반품되지 않은 건)
        List<Orders> eligibleOrders = ordersRepository.findByOrderStatusAndUpdatedAtBefore(OrderStatus.DELIVERED, cutoffDate);

        // 3. 판매자 ID별로 주문 그룹화
        Map<Integer, List<Orders>> ordersBySellerId = eligibleOrders.stream()
                .filter(order -> order.getAuction() != null && order.getAuction().getProductInspection() != null)
                .collect(Collectors.groupingBy(order -> order.getAuction().getProductInspection().getMemberId()));

        // 4. 각 판매자별로 정산 처리
        for (Map.Entry<Integer, List<Orders>> entry : ordersBySellerId.entrySet()) {
            Integer sellerId = entry.getKey();
            List<Orders> sellerOrders = entry.getValue();

            Member seller = memberRepository.findById(sellerId)
                    .orElse(null);
            if (seller == null) {
                continue; // 판매자 정보가 없으면 정산 건너뛰기
            }

            // 기본 수수료율 조회
            SettlementSetting setting = settlementSettingRepository.findFirstByOrderByCreatedAtDesc()
                    .orElseThrow(() -> new SettlementException(SettlementErrorCode.SETTING_NOT_FOUND));
            BigDecimal commissionRate = setting.getCommissionRate().divide(new BigDecimal("100"));

            BigDecimal totalSaleAmount = sellerOrders.stream()
                    .map(order -> new BigDecimal(order.getPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalCommission = totalSaleAmount.multiply(commissionRate);
            BigDecimal finalSettlementAmount = totalSaleAmount.subtract(totalCommission);

            Settlement settlement = Settlement.builder()
                    .member(seller)
                    .totalAmount(totalSaleAmount)
                    .commissionAmount(totalCommission)
                    .settlementAmount(finalSettlementAmount)
                    .status(SettlementStatus.PENDING)
                    .requestedAt(LocalDateTime.now())
                    .build();

            List<SettlementItem> items = sellerOrders.stream().map(order -> {
                BigDecimal itemCommission = new BigDecimal(order.getPrice()).multiply(commissionRate);
                return SettlementItem.builder()
                        .settlement(settlement)
                        .order(order)
                        .itemSaleAmount(new BigDecimal(order.getPrice()))
                        .commissionRate(commissionRate)
                        .commissionAmount(itemCommission)
                        .totalAmount(new BigDecimal(order.getPrice()).subtract(itemCommission))
                        .isReturned(false)
                        .build();
            }).collect(Collectors.toList());

            settlement.getSettlementItems().addAll(items);

            settlementRepository.save(settlement);

            RevenueLedger ledgerEntry = RevenueLedger.builder()
                    .transactionAt(LocalDateTime.now())
                    .revenueType(RevenueType.COMMISSION)
                    .amount(totalCommission)
                    .relatedSettlement(settlement)
                    .description("정산 ID: " + settlement.getSettlementId() + "에 대한 판매 수수료")
                    .build();
            revenueLedgerRepository.save(ledgerEntry);
        }
    }


    /**
     * 현재 적용 중인 정산 설정을 조회합니다。
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
        return settlementSettingRepository.findFirstByOrderByCreatedAtDesc()
                .map(existingSetting -> {
                    existingSetting.update(
                            request.commissionRate(),
                            request.settlementCycleType(),
                            request.settlementDay(),
                            admin
                    );
                    return SettlementSettingResponse.from(existingSetting);
                })
                .orElseGet(() -> {
                    SettlementSetting newSetting = request.toEntity(admin);
                    SettlementSetting savedSetting = settlementSettingRepository.save(newSetting);
                    return SettlementSettingResponse.from(savedSetting);
                });
    }
}
