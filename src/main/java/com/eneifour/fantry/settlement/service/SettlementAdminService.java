package com.eneifour.fantry.settlement.service;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.repository.MemberRepository;
import com.eneifour.fantry.orders.domain.Orders;
import com.eneifour.fantry.orders.domain.OrderStatus;
import com.eneifour.fantry.orders.repository.OrdersRepository;
import com.eneifour.fantry.settlement.domain.*;
import com.eneifour.fantry.settlement.domain.SettlementSetting;
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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
                .orElseThrow(() -> new SettlementException(SettlementErrorCode.SETTLEMENT_NOT_FOUND));
        return SettlementDetailResponse.from(settlement);
    }


    /**
     * 특정 날짜를 기준으로 정산을 실행합니다.
     * @param settlementDate 정산 기준일
     */
    public void executeSettlement(LocalDate settlementDate) {
        // 1. 정산 대상 기간 정의 (예: 정산일 기준 7일 이전까지 배송 완료된 건)
        LocalDateTime cutoffDate = settlementDate.atStartOfDay().minusDays(7);

        // 2. 정산 대상 주문 조회 (배송 완료 상태)
        // XXX: `updatedAt`이 아닌 `deliveredAt` 기준으로 조회하는 것이 더 정확합니다.
        //      향후 `OrdersRepository`에 `findByOrderStatusAndDeliveredAtBefore` 메서드 추가를 권장합니다.
        List<Orders> ordersDelivered = ordersRepository.findByOrderStatusAndUpdatedAtBefore(OrderStatus.DELIVERED, cutoffDate);

        // 추가 필터링: deliveredAt이 cutoffDate 이전인지 다시 확인하여 정확성 확보
        List<Orders> eligibleOrders = ordersDelivered.stream()
                .filter(order -> order.getDeliveredAt() != null && order.getDeliveredAt().isBefore(cutoffDate))
                .collect(Collectors.toList());

        // 3. 판매자 ID별로 주문 그룹화
        // XXX: `Orders` 엔티티에 Seller(판매자)를 직접 연결하는 것이 좋습니다.
        //      현재 구조는 경매를 통한 판매에만 의존하고 있어, 다른 판매 방식 추가 시 정산 로직이 동작하지 않을 수 있습니다.
        Map<Integer, List<Orders>> ordersBySellerId = eligibleOrders.stream()
                .filter(order -> order.getAuction() != null && order.getAuction().getProductInspection() != null)
                .collect(Collectors.groupingBy(order -> order.getAuction().getProductInspection().getMemberId()));

        // 4. 각 판매자별로 정산 처리
        for (Map.Entry<Integer, List<Orders>> entry : ordersBySellerId.entrySet()) {
            Integer sellerId = entry.getKey();
            List<Orders> sellerOrders = entry.getValue();

            Member seller = memberRepository.findById(sellerId).orElse(null);
            if (seller == null) {
                log.warn("판매자 정보를 찾을 수 없어 정산을 건너뜁니다. sellerId: {}", sellerId);
                continue;
            }

            // 활성 수수료 규칙 조회
            LocalDateTime now = LocalDateTime.now();
            List<CommissionRule> activeCommissionRules = commissionRuleRepository.findByIsActiveTrueAndStartDateBeforeAndEndDateAfterOrderByPriorityAsc(now, now);

            if (activeCommissionRules.isEmpty()) {
                throw new SettlementException(SettlementErrorCode.COMMISSION_RULE_NOT_FOUND); // 새로운 에러 코드 필요
            }

            // 가장 높은 우선순위의 규칙 적용 (OrderByPriorityAsc 이므로 첫 번째가 가장 높은 우선순위)
            CommissionRule applicableRule = activeCommissionRules.get(0);
            BigDecimal commissionRate = applicableRule.getCommissionRate().divide(new BigDecimal("100"));

            // 5. 실제 결제 금액(Payment) 기준으로 정산 금액 계산
            List<Orders> validOrders = sellerOrders.stream()
                    .filter(o -> o.getPayment() != null && o.getPayment().getPrice() != null)
                    .collect(Collectors.toList());

            if (validOrders.isEmpty()) {
                continue;
            }

            BigDecimal totalSaleAmount = validOrders.stream()
                    .map(order -> new BigDecimal(order.getPayment().getPrice()))
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

            List<SettlementItem> items = validOrders.stream().map(order -> {
                BigDecimal itemPrice = new BigDecimal(order.getPayment().getPrice());
                BigDecimal itemCommission = itemPrice.multiply(commissionRate);
                return SettlementItem.builder()
                        .settlement(settlement)
                        .order(order)
                        .itemSaleAmount(itemPrice)
                        .commissionRate(commissionRate)
                        .commissionAmount(itemCommission)
                        .totalAmount(itemPrice.subtract(itemCommission))
                        .isReturned(false) // TODO: 환불/반품 정책 확정 후 연동 필요
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
