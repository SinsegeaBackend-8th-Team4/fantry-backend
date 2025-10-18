package com.eneifour.fantry.settlement.service;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.orders.domain.Orders;
import com.eneifour.fantry.orders.domain.OrderStatus;
import com.eneifour.fantry.orders.repository.OrdersRepository;
import com.eneifour.fantry.refund.domain.ReturnRequest;
import com.eneifour.fantry.refund.domain.ReturnStatus;
import com.eneifour.fantry.refund.repository.ReturnRepository;
import com.eneifour.fantry.settlement.domain.*;
import com.eneifour.fantry.settlement.dto.SettlementDetailResponse;
import com.eneifour.fantry.settlement.dto.SettlementSummaryResponse;
import com.eneifour.fantry.settlement.exception.SettlementErrorCode;
import com.eneifour.fantry.settlement.exception.SettlementException;
import com.eneifour.fantry.settlement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 판매자용 정산 관련 서비스를 담당합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final OrdersRepository ordersRepository;
    private final ReturnRepository returnRepository;
    private final SettlementSettingRepository settlementSettingRepository;
    private final CommissionRuleRepository commissionRuleRepository;
    private final SettlementItemRepository settlementItemRepository;
    private final RevenueLedgerRepository revenueLedgerRepository;

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

    /**
     * 지정된 기간 동안의 정산을 처리합니다.
     * @param startDate 정산 시작일
     * @param endDate 정산 종료일
     */
    @Transactional
    public void processSettlements(LocalDateTime startDate, LocalDateTime endDate) {
        SettlementSetting defaultSetting = getSettlementSetting();
        List<Orders> completedOrders = getCompletedOrdersForPeriod(startDate, endDate);
        Map<Integer, ReturnRequest> completedReturnsMap = getCompletedReturnsForPeriod(startDate, endDate);

        Map<Member, List<Orders>> ordersBySeller = completedOrders.stream()
                .collect(Collectors.groupingBy(Orders::getMember));

        for (Map.Entry<Member, List<Orders>> entry : ordersBySeller.entrySet()) {
            Member seller = entry.getKey();
            List<Orders> sellerOrders = entry.getValue();

            BigDecimal totalSalesAmount = BigDecimal.ZERO;
            BigDecimal totalCommissionAmount = BigDecimal.ZERO;
            BigDecimal totalRefundAmount = BigDecimal.ZERO;

            Settlement settlement = Settlement.builder()
                    .member(seller)
                    .status(SettlementStatus.PENDING)
                    .requestedAt(LocalDateTime.now())
                    .build();
            settlement = settlementRepository.save(settlement);

            for (Orders order : sellerOrders) {
                BigDecimal itemSaleAmount = BigDecimal.valueOf(order.getPrice());
                BigDecimal appliedCommissionRate = defaultSetting.getCommissionRate();

                // TODO: CommissionRule을 사용하여 동적 수수료율 적용 로직 추가 필요

                BigDecimal commissionAmount = itemSaleAmount.multiply(appliedCommissionRate);
                BigDecimal netSaleAmount = itemSaleAmount.subtract(commissionAmount);

                boolean isReturned = completedReturnsMap.containsKey(order.getOrdersId());
                if (isReturned) {
                    ReturnRequest returnRequest = completedReturnsMap.get(order.getOrdersId());
                    netSaleAmount = BigDecimal.ZERO;
                    totalRefundAmount = totalRefundAmount.add(returnRequest.getFinalRefundAmount());
                }

                totalSalesAmount = totalSalesAmount.add(itemSaleAmount);
                totalCommissionAmount = totalCommissionAmount.add(commissionAmount);

                SettlementItem item = SettlementItem.builder()
                        .settlement(settlement)
                        .order(order)
                        .returnRequest(isReturned ? completedReturnsMap.get(order.getOrdersId()) : null)
                        .itemSaleAmount(itemSaleAmount)
                        .commissionRate(appliedCommissionRate)
                        .commissionAmount(commissionAmount)
                        .totalAmount(netSaleAmount)
                        .isReturned(isReturned)
                        .build();
                settlementItemRepository.save(item);

                // RevenueLedger 기록 (수수료 비용)
                revenueLedgerRepository.save(RevenueLedger.builder()
                        .transactionAt(order.getCreatedAt())
                        .revenueType(RevenueType.COMMISSION)
                        .amount(commissionAmount) // 플랫폼 수익이므로 양수
                        .relatedOrder(order)
                        .relatedSettlement(settlement)
                        .description("판매 수수료")
                        .build());

                // order.changeStatus(OrderStatus.SETTLED); // TODO: OrderStatus에 SETTLED 추가 필요
                // ordersRepository.save(order);
            }

            settlement.completeSettlement(
                    totalSalesAmount,
                    totalCommissionAmount,
                    totalRefundAmount,
                    totalSalesAmount.subtract(totalCommissionAmount).subtract(totalRefundAmount),
                    LocalDateTime.now()
            );
            settlementRepository.save(settlement);

            // RevenueLedger 기록 (정산 지급)
            revenueLedgerRepository.save(RevenueLedger.builder()
                    .transactionAt(settlement.getCompletedAt())
                    .revenueType(RevenueType.SETTLEMENT_PAYOUT)
                    .amount(settlement.getSettlementAmount().negate()) // 지급이므로 음수
                    .relatedSettlement(settlement)
                    .description("판매자 정산 지급")
                    .build());
        }
    }

    private SettlementSetting getSettlementSetting() {
        return settlementSettingRepository.findFirstByOrderByCreatedAtDesc()
                .orElseThrow(() -> new SettlementException(SettlementErrorCode.SETTLEMENT_SETTING_NOT_FOUND));
    }

    private List<Orders> getCompletedOrdersForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return ordersRepository.findByOrderStatusAndCreatedAtBetween(OrderStatus.CONFIRMED, startDate, endDate);
    }

    private Map<Integer, ReturnRequest> getCompletedReturnsForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return returnRepository.findByStatusAndCompletedAtBetween(ReturnStatus.COMPLETED, startDate, endDate)
                .stream()
                .collect(Collectors.toMap(r -> r.getOrders().getOrdersId(), r -> r));
    }
}
