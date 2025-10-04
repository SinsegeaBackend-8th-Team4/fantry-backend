package com.eneifour.fantry.report.repository;

import com.eneifour.fantry.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    public List<Report> findReportsByMemberId(int memberId);
    public Report findById(int reportId);
}
