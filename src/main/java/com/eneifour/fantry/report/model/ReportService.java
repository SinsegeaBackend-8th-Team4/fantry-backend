package com.eneifour.fantry.report.model;

import com.eneifour.fantry.report.domain.Report;
import com.eneifour.fantry.report.domain.ReportStatus;
import com.eneifour.fantry.report.exception.ReportErrorCode;
import com.eneifour.fantry.report.exception.ReportException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final JpaReportRepository jpaReportRepository;

    //모든 신고 내역 가져오기
    public List<Report> getReports(){
        return jpaReportRepository.findAll();
    }

    //한명의 회원 신고 내역 가져오기
    public List<Report> getReportsByMemberId(int memberId){
        return jpaReportRepository.findReportsByMemberId(memberId);
    }

    //하나의 특정 신고 내역 가져오기
    public Report getReport(int reportId){
        return jpaReportRepository.findById(reportId);
    }

    //신고 추가하기
    public void saveReport(Report report){
        if(report.getReportStatus() == null){
            report.setReportStatus(ReportStatus.RECEIVED);
        }
        jpaReportRepository.save(report);
    }

    //신고 수정하기
    @Transactional
    public void updateReport(Report report) throws ReportException {
        if(!jpaReportRepository.existsById(report.getReportId())){
            throw new ReportException(ReportErrorCode.REPORT_NOT_FOUND);
        }
        jpaReportRepository.save(report);
    }

    //신고 삭제하기
    @Transactional
    public void deleteReport(int reportId) throws ReportException {
        if(!jpaReportRepository.existsById(reportId)){
            throw new ReportException(ReportErrorCode.REPORT_NOT_FOUND);
        }
        jpaReportRepository.deleteById(reportId);
    }
}
