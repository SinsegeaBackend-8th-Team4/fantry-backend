package com.eneifour.fantry.report.model;

import com.eneifour.fantry.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaReportRepository extends JpaRepository<Report, Integer> {
    public List<Report> findReportsByMemberId(int memberId);
    public Report findById(int reportId);
}
