package com.eneifour.fantry.dashboard.service;

import com.eneifour.fantry.inquiry.domain.InquiryStatus;
import com.eneifour.fantry.inquiry.repository.InquiryRepository;
import com.eneifour.fantry.dashboard.dto.DashboardSummaryResponse;
import com.eneifour.fantry.dashboard.dto.InquiryStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DashboardService (신규)
 * 여러 도메인의 데이터를 취합하여 대시보드에 필요한 요약 정보를 제공하는 서비스.
 * 각 도메인의 Repository를 직접 참조하여 가벼운 COUNT 쿼리를 실행한다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final InquiryRepository inquiryRepository;
    // private final RefundRepository refundRepository; // 향후 환불 기능 추가 시 주입

    /**
     * 대시보드에 필요한 모든 요약 정보를 조회하여 반환한다.
     * @return DashboardSummaryResponse DTO
     */
    public DashboardSummaryResponse getDashboardSummary() {
        InquiryStats inquiryStats = getInquiryStats();
        // RefundStats refundStats = getRefundStats(); // 향후 확장

        return new DashboardSummaryResponse(inquiryStats);
    }

    /**
     * 문의(Inquiry) 관련 통계 데이터를 조회한다.
     * @return InquiryStats DTO
     */
    private InquiryStats getInquiryStats() {
        // 오늘 날짜의 시작(00:00:00)과 끝(23:59:59) 시간 계산
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        // Repository의 count 메서드를 호출하여 각 통계치 계산
        long todayNew = inquiryRepository.countByCreatedAtBetween(startOfDay, endOfDay);
        long pending = inquiryRepository.countByStatus(InquiryStatus.PENDING);
        long inProgress = inquiryRepository.countByStatus(InquiryStatus.IN_PROGRESS);

        return new InquiryStats(todayNew, pending, inProgress);
    }
}
