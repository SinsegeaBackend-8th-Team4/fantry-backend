package com.eneifour.fantry.dashboard.repository;

import com.eneifour.fantry.member.domain.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface DashboardRepository extends org.springframework.data.repository.Repository<Member, Integer> {

    @Query(value = "SELECT " +
            "    COALESCE((SELECT COUNT(*) FROM member), 0) AS totalUsers, " +
            "    COALESCE((SELECT COUNT(*) FROM member WHERE DATE(create_at) = CURDATE()), 0) AS todayRegisteredUsers, " +
            "    COALESCE((SELECT COUNT(*) FROM auction), 0) AS totalAuctions, " +
            "    COALESCE((SELECT COUNT(*) FROM auction WHERE sale_status = 'ACTIVE'), 0) AS ongoingAuctions, " +
            "    COALESCE((SELECT COUNT(*) FROM inquiry), 0) AS totalInquiries, " +
            "    COALESCE((SELECT COUNT(*) FROM inquiry WHERE status = 'UNANSWERED'), 0) AS unansweredInquiries, " +
            "    COALESCE((SELECT COUNT(*) FROM payment), 0) AS totalPayments, " +
            "    COALESCE((SELECT COUNT(*) FROM payment WHERE DATE(purchased_at) = CURDATE()), 0) AS todayPayments, " +
            "    COALESCE((SELECT COUNT(*) FROM settlement), 0) AS totalSettlements, " +
            "    COALESCE((SELECT COUNT(*) FROM settlement WHERE status = 'PENDING'), 0) AS pendingSettlements, " +
            "    COALESCE((SELECT COUNT(*) FROM return_requests WHERE status != 'DELETED'), 0) AS totalRefunds, " +
            "    COALESCE((SELECT COUNT(*) FROM return_requests WHERE status = 'REQUESTED'), 0) AS requestedRefunds, " +
            "    COALESCE((SELECT COUNT(*) FROM notice), 0) AS totalNotices, " +
            "    COALESCE((SELECT COUNT(*) FROM notice WHERE status = 'ACTIVE'), 0) AS activeNotices, " +
            "    COALESCE((SELECT COUNT(*) FROM faq), 0) AS totalFaqs, " +
            "    COALESCE((SELECT COUNT(*) FROM faq WHERE status = 'ACTIVE'), 0) AS activeFaqs, " +
            "    COALESCE((SELECT COUNT(*) FROM bid), 0) AS totalBids, " +
            "    COALESCE((SELECT COUNT(*) FROM bid WHERE DATE(bid_at) = CURDATE()), 0) AS bidsToday, " +
            "    COALESCE((SELECT COUNT(*) FROM account), 0) AS totalAccounts, " +
            "    COALESCE((SELECT COUNT(*) FROM account WHERE is_active = '1'), 0) AS activeAccounts, " +
            "    COALESCE((SELECT COUNT(*) FROM artist), 0) AS totalArtists, " +
            "    COALESCE((SELECT COUNT(*) FROM artist WHERE status = 'APPROVED'), 0) AS approvedArtists, " +
            "    COALESCE((SELECT COUNT(*) FROM product_inspection), 0) AS totalInspections, " +
            "    COALESCE((SELECT COUNT(*) FROM product_inspection WHERE inspection_status = 'SUBMITTED'), 0) AS submittedInspections, " +
            "    COALESCE((SELECT COUNT(*) FROM checklist_template), 0) AS totalChecklistTemplates, " +
            "    COALESCE((SELECT COUNT(*) FROM report), 0) AS totalReports, " +
            "    COALESCE((SELECT COUNT(*) FROM report WHERE report_status = 'RECEIVED'), 0) AS receivedReports", nativeQuery = true)
    Map<String, Object> getIntegratedDashboardData();
}
