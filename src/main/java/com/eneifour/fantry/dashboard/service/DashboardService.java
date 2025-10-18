package com.eneifour.fantry.dashboard.service;

import com.eneifour.fantry.account.repository.AccountRepository;
import com.eneifour.fantry.bid.repository.BidRepository;
import com.eneifour.fantry.catalog.repository.AlbumRepository;
import com.eneifour.fantry.catalog.repository.ArtistRepository;
import com.eneifour.fantry.catalog.repository.GoodsCategoryRepository;
import com.eneifour.fantry.checklist.repository.ChecklistItemRepository;
import com.eneifour.fantry.checklist.repository.ChecklistTemplateRepository;
import com.eneifour.fantry.dashboard.dto.*;
import com.eneifour.fantry.dashboard.repository.DashboardRepository;
import com.eneifour.fantry.faq.repository.FaqRepository;
import com.eneifour.fantry.inspection.repository.InspectionRepository;
import com.eneifour.fantry.inquiry.domain.InquiryStatus;
import com.eneifour.fantry.inquiry.repository.InquiryRepository;
import com.eneifour.fantry.member.repository.MemberRepository;
import com.eneifour.fantry.notice.repository.NoticeRepository;
import com.eneifour.fantry.orders.domain.OrderStatus;
import com.eneifour.fantry.orders.repository.OrdersRepository;
import com.eneifour.fantry.refund.repository.ReturnRepository;
import com.eneifour.fantry.report.repository.ReportRepository;
import com.eneifour.fantry.settlement.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final OrdersRepository ordersRepository;
    private final DashboardRepository dashboardRepository;
    private final SettlementRepository settlementRepository;
    private final ReturnRepository returnRepository;
    private final NoticeRepository noticeRepository;
    private final FaqRepository faqRepository;
    private final BidRepository bidRepository;
    private final AccountRepository accountRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final GoodsCategoryRepository goodsCategoryRepository;
    private final InspectionRepository inspectionRepository;
    private final ChecklistTemplateRepository checklistTemplateRepository;
    private final ChecklistItemRepository checklistItemRepository;
    private final ReportRepository reportRepository;
    private final InquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;

    public OrdersStats getOrdersDashboard() {
        Map<String, Long> ordersByStatus = ordersRepository.countOrdersByStatus();
        return OrdersStats.builder()
                .totalOrders(ordersByStatus.getOrDefault("totalOrders", 0L))
                .pendingPaymentOrders(ordersByStatus.getOrDefault("pendingPaymentOrders", 0L))
                .paidOrders(ordersByStatus.getOrDefault("paidOrders", 0L))
                .preparingShipmentOrders(ordersByStatus.getOrDefault("preparingShipmentOrders", 0L))
                .shippedOrders(ordersByStatus.getOrDefault("shippedOrders", 0L))
                .deliveredOrders(ordersByStatus.getOrDefault("deliveredOrders", 0L))
                .confirmedOrders(ordersByStatus.getOrDefault("confirmedOrders", 0L))
                .cancelRequestedOrders(ordersByStatus.getOrDefault("cancelRequestedOrders", 0L))
                .cancelledOrders(ordersByStatus.getOrDefault("cancelledOrders", 0L))
                .refundRequestedOrders(ordersByStatus.getOrDefault("refundRequestedOrders", 0L))
                .refundedOrders(ordersByStatus.getOrDefault("refundedOrders", 0L))
                .build();
    }

    public DashboardStats getIntegratedDashboard() {
        Map<String, Object> data = dashboardRepository.getIntegratedDashboardData();
        return DashboardStats.builder()
                .totalUsers(((Long) data.getOrDefault("totalUsers", 0L)))
                .todayRegisteredUsers(((Long) data.getOrDefault("todayRegisteredUsers", 0L)))
                .totalAuctions(((Long) data.getOrDefault("totalAuctions", 0L)))
                .ongoingAuctions(((Long) data.getOrDefault("ongoingAuctions", 0L)))
                .totalInquiries(((Long) data.getOrDefault("totalInquiries", 0L)))
                .unansweredInquiries(((Long) data.getOrDefault("unansweredInquiries", 0L)))
                .totalPayments(((Long) data.getOrDefault("totalPayments", 0L)))
                .todayPayments(((Long) data.getOrDefault("todayPayments", 0L)))
                .totalSettlements(((Long) data.getOrDefault("totalSettlements", 0L)))
                .pendingSettlements(((Long) data.getOrDefault("pendingSettlements", 0L)))
                .totalRefunds(((Long) data.getOrDefault("totalRefunds", 0L)))
                .requestedRefunds(((Long) data.getOrDefault("requestedRefunds", 0L)))
                .totalNotices(((Long) data.getOrDefault("totalNotices", 0L)))
                .activeNotices(((Long) data.getOrDefault("activeNotices", 0L)))
                .totalFaqs(((Long) data.getOrDefault("totalFaqs", 0L)))
                .activeFaqs(((Long) data.getOrDefault("activeFaqs", 0L)))
                .totalBids(((Long) data.getOrDefault("totalBids", 0L)))
                .bidsToday(((Long) data.getOrDefault("bidsToday", 0L)))
                .totalAccounts(((Long) data.getOrDefault("totalAccounts", 0L)))
                .activeAccounts(((Long) data.getOrDefault("activeAccounts", 0L)))
                .totalArtists(((Long) data.getOrDefault("totalArtists", 0L)))
                .approvedArtists(((Long) data.getOrDefault("approvedArtists", 0L)))
                .totalInspections(((Long) data.getOrDefault("totalInspections", 0L)))
                .submittedInspections(((Long) data.getOrDefault("submittedInspections", 0L)))
                .totalChecklistTemplates(((Long) data.getOrDefault("totalChecklistTemplates", 0L)))
                .totalReports(((Long) data.getOrDefault("totalReports", 0L)))
                .receivedReports(((Long) data.getOrDefault("receivedReports", 0L)))
                .build();
    }

    public SettlementStats getSettlementStats() {
        Map<String, Long> settlementByStatus = settlementRepository.countSettlementsByStatus();
        return SettlementStats.builder()
                .totalSettlements(settlementByStatus.getOrDefault("totalSettlements", 0L))
                .pendingSettlements(settlementByStatus.getOrDefault("pendingSettlements", 0L))
                .paidSettlements(settlementByStatus.getOrDefault("paidSettlements", 0L))
                .cancelledSettlements(settlementByStatus.getOrDefault("cancelledSettlements", 0L))
                .failedSettlements(settlementByStatus.getOrDefault("failedSettlements", 0L))
                .build();
    }

    public RefundStats getRefundStats() {
        Map<String, Long> refundByStatus = returnRepository.countRefundsByStatus();
        return RefundStats.builder()
                .totalRefunds(refundByStatus.getOrDefault("totalRefunds", 0L))
                .requestedRefunds(refundByStatus.getOrDefault("requestedRefunds", 0L))
                .inTransitRefunds(refundByStatus.getOrDefault("inTransitRefunds", 0L))
                .inspectingRefunds(refundByStatus.getOrDefault("inspectingRefunds", 0L))
                .approvedRefunds(refundByStatus.getOrDefault("approvedRefunds", 0L))
                .rejectedRefunds(refundByStatus.getOrDefault("rejectedRefunds", 0L))
                .completedRefunds(refundByStatus.getOrDefault("completedRefunds", 0L))
                .userCancelledRefunds(refundByStatus.getOrDefault("userCancelledRefunds", 0L))
                .build();
    }

    public NoticeStats getNoticeStats() {
        Map<String, Long> noticeByStatus = noticeRepository.countNoticesByStatus();
        return NoticeStats.builder()
                .totalNotices(noticeByStatus.getOrDefault("totalNotices", 0L))
                .draftNotices(noticeByStatus.getOrDefault("draftNotices", 0L))
                .activeNotices(noticeByStatus.getOrDefault("activeNotices", 0L))
                .pinnedNotices(noticeByStatus.getOrDefault("pinnedNotices", 0L))
                .inactiveNotices(noticeByStatus.getOrDefault("inactiveNotices", 0L))
                .build();
    }

    public FaqStats getFaqStats() {
        Map<String, Long> faqByStatus = faqRepository.countFaqsByStatus();
        return FaqStats.builder()
                .totalFaqs(faqByStatus.getOrDefault("totalFaqs", 0L))
                .draftFaqs(faqByStatus.getOrDefault("draftFaqs", 0L))
                .activeFaqs(faqByStatus.getOrDefault("activeFaqs", 0L))
                .pinnedFaqs(faqByStatus.getOrDefault("pinnedFaqs", 0L))
                .inactiveFaqs(faqByStatus.getOrDefault("inactiveFaqs", 0L))
                .build();
    }

    public InquiryStats getInquiryStats() {
        long totalInquiries = inquiryRepository.count();
        long pendingInquiries = inquiryRepository.countByStatus(InquiryStatus.PENDING);
        long inProgressInquiries = inquiryRepository.countByStatus(InquiryStatus.IN_PROGRESS);
        long onHoldInquiries = inquiryRepository.countByStatus(InquiryStatus.ON_HOLD);
        long rejectedInquiries = inquiryRepository.countByStatus(InquiryStatus.REJECTED);
        long answeredInquiries = inquiryRepository.countByStatus(InquiryStatus.ANSWERED);

        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
        long todayInquiries = inquiryRepository.countByInquiredAtBetween(startOfDay, endOfDay);

        return InquiryStats.builder()
                .totalInquiries(totalInquiries)
                .pendingInquiries(pendingInquiries)
                .inProgressInquiries(inProgressInquiries)
                .onHoldInquiries(onHoldInquiries)
                .rejectedInquiries(rejectedInquiries)
                .answeredInquiries(answeredInquiries)
                .todayInquiries(todayInquiries)
                .build();
    }

    public CSDashboardStats getCSDashboardStats() {
        return CSDashboardStats.builder()
                .noticeStats(getNoticeStats())
                .faqStats(getFaqStats())
                .inquiryStats(getInquiryStats())
                .build();
    }

    public BidStats getBidStats() {
        Map<String, Object> bidStats = bidRepository.getBidStats();
        return BidStats.builder()
                .totalBids(((Long) bidStats.getOrDefault("totalBids", 0L)))
                .bidsOnActiveAuctions(((Long) bidStats.getOrDefault("bidsOnActiveAuctions", 0L)))
                .bidsToday(((Long) bidStats.getOrDefault("bidsToday", 0L)))
                .build();
    }

    public AccountStats getAccountStats() {
        Map<String, Long> accountByStatus = accountRepository.countAccountsByStatus();
        return AccountStats.builder()
                .totalAccounts(accountByStatus.getOrDefault("totalAccounts", 0L))
                .activeAccounts(accountByStatus.getOrDefault("activeAccounts", 0L))
                .inactiveAccounts(accountByStatus.getOrDefault("inactiveAccounts", 0L))
                .refundableAccounts(accountByStatus.getOrDefault("refundableAccounts", 0L))
                .nonRefundableAccounts(accountByStatus.getOrDefault("nonRefundableAccounts", 0L))
                .build();
    }

    public MemberStats getMemberStats() {
        Map<String, Long> memberByStatus = memberRepository.countMemberStats();
        return MemberStats.builder()
                .totalMembers(memberByStatus.getOrDefault("totalMembers", 0L))
                .todayRegisteredMembers(memberByStatus.getOrDefault("todayRegisteredMembers", 0L))
                .build();
    }

    public CatalogStats getCatalogStats() {
        Map<String, Long> artistStats = artistRepository.countArtistsByStatus();
        long totalAlbums = albumRepository.count();
        long totalGoodsCategories = goodsCategoryRepository.count();

        return CatalogStats.builder()
                .totalArtists(artistStats.getOrDefault("totalArtists", 0L))
                .pendingArtists(artistStats.getOrDefault("pendingArtists", 0L))
                .approvedArtists(artistStats.getOrDefault("approvedArtists", 0L))
                .rejectedArtists(artistStats.getOrDefault("rejectedArtists", 0L))
                .totalAlbums(totalAlbums)
                .totalGoodsCategories(totalGoodsCategories)
                .build();
    }

    public InspectionStats getInspectionStats() {
        Map<String, Long> inspectionByStatus = inspectionRepository.countInspectionsByStatus();
        return InspectionStats.builder()
                .totalInspections(inspectionByStatus.getOrDefault("totalInspections", 0L))
                .draftInspections(inspectionByStatus.getOrDefault("draftInspections", 0L))
                .submittedInspections(inspectionByStatus.getOrDefault("submittedInspections", 0L))
                .onlineApprovedInspections(inspectionByStatus.getOrDefault("onlineApprovedInspections", 0L))
                .onlineRejectedInspections(inspectionByStatus.getOrDefault("onlineRejectedInspections", 0L))
                .offlineInspectingInspections(inspectionByStatus.getOrDefault("offlineInspectingInspections", 0L))
                .offlineRejectedInspections(inspectionByStatus.getOrDefault("offlineRejectedInspections", 0L))
                .completedInspections(inspectionByStatus.getOrDefault("completedInspections", 0L))
                .build();
    }

    public ChecklistStats getChecklistStats() {
        long totalTemplates = checklistTemplateRepository.count();
        long totalItems = checklistItemRepository.count();
        return ChecklistStats.builder()
                .totalChecklistTemplates(totalTemplates)
                .totalChecklistItems(totalItems)
                .build();
    }

    public CatalogDashboardStats getCatalogDashboardStats() {
        return CatalogDashboardStats.builder()
                .catalogStats(getCatalogStats())
                .checklistStats(getChecklistStats())
                .build();
    }

    public ReportStats getReportStats() {
        Map<String, Long> reportByStatus = reportRepository.countReportsByStatus();
        return ReportStats.builder()
                .totalReports(reportByStatus.getOrDefault("totalReports", 0L))
                .resolvedReports(reportByStatus.getOrDefault("resolvedReports", 0L))
                .receivedReports(reportByStatus.getOrDefault("receivedReports", 0L))
                .withdrawnReports(reportByStatus.getOrDefault("withdrawnReports", 0L))
                .rejectedReports(reportByStatus.getOrDefault("rejectedReports", 0L))
                .build();
    }

    public SalesStats getSalesStats() {
        long totalSalesProducts = ordersRepository.countByOrderStatus(com.eneifour.fantry.orders.domain.OrderStatus.DELIVERED);
        BigDecimal totalSoldAmount = ordersRepository.sumPriceByOrderStatus(OrderStatus.DELIVERED);

        return SalesStats.builder()
                .totalSalesProducts(totalSalesProducts)
                .totalSoldAmount(totalSoldAmount != null ? totalSoldAmount : BigDecimal.ZERO)
                .build();
    }

    public FinanceOperationsDashboardStats getFinanceOperationsDashboardStats() {
        return FinanceOperationsDashboardStats.builder()
                .settlementStats(getSettlementStats())
                .refundStats(getRefundStats())
                .build();
    }

    public MemberDashboardStats getMemberDashboardStats() {
        return MemberDashboardStats.builder()
                .memberStats(getMemberStats())
                .reportStats(getReportStats())
                .build();
    }
}