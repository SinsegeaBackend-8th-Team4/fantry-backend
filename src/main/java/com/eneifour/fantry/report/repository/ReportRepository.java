package com.eneifour.fantry.report.repository;

import com.eneifour.fantry.report.domain.Report;
import com.eneifour.fantry.report.domain.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    public List<Report> findByMember_MemberId(int memberId);
    public Report findByReportId(int reportId);
    public List<Report> findByReportStatus(ReportStatus reportStatus);  //구제 신청 리스트
}
