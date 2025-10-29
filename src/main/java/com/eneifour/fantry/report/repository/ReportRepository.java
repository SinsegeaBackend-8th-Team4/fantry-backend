package com.eneifour.fantry.report.repository;

import com.eneifour.fantry.report.domain.Report;
import com.eneifour.fantry.report.domain.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    public List<Report> findByMember_MemberId(int memberId);
    public Report findByReportId(int reportId);
    public List<Report> findByReportStatus(ReportStatus reportStatus);  //구제 신청 리스트

    @Query("SELECT new map(" +
            "   count(r) as totalReports, " +
            "   COALESCE(sum(case when r.reportStatus = com.eneifour.fantry.report.domain.ReportStatus.RESOLVED then 1 else 0 end), 0L) as resolvedReports, " +
            "   COALESCE(sum(case when r.reportStatus = com.eneifour.fantry.report.domain.ReportStatus.RECEIVED then 1 else 0 end), 0L) as receivedReports, " +
            "   COALESCE(sum(case when r.reportStatus = com.eneifour.fantry.report.domain.ReportStatus.WITHDRAWN then 1 else 0 end), 0L) as withdrawnReports, " +
            "   COALESCE(sum(case when r.reportStatus = com.eneifour.fantry.report.domain.ReportStatus.REJECTED then 1 else 0 end), 0L) as rejectedReports) " +
            "FROM Report r")
    Map<String, Long> countReportsByStatus();
}
