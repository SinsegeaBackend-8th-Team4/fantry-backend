package com.eneifour.fantry.report.service;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.model.JpaMemberRepository;
import com.eneifour.fantry.report.domain.Report;
import com.eneifour.fantry.report.domain.ReportStatus;
import com.eneifour.fantry.report.dto.ReportRequest;
import com.eneifour.fantry.report.dto.ReportResponse;
import com.eneifour.fantry.report.exception.ReportErrorCode;
import com.eneifour.fantry.report.exception.ReportException;
import com.eneifour.fantry.report.repository.ReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final JpaMemberRepository memberRepository;

    //모든 신고 내역 가져오기
    public List<ReportResponse> getReports(){
        return ReportResponse.fromList(reportRepository.findAll());
    }

    //한명의 회원 신고 내역 가져오기
    public List<ReportResponse> getReportsByMemberId(int memberId){
        return ReportResponse.fromList(reportRepository.findReportsByMemberId(memberId));
    }

    //하나의 특정 신고 내역 가져오기
    public ReportResponse getReport(int reportId){
        Report report = reportRepository.findById(reportId);
        if(report == null){
            throw new ReportException(ReportErrorCode.REPORT_NOT_FOUND);
        }
        return ReportResponse.from(report);
    }

    //신고 추가하기
    @Transactional
    public void saveReport(ReportRequest reportRequest){
        Member member = memberRepository.findByMemberId(reportRequest.getMemberId());
        if(member == null){
            throw new ReportException(ReportErrorCode.REPORT_NOT_FOUND_MEMBER);
        }

        Report report = Report.builder()
                .reportReason(reportRequest.getReportReason())
                .reportStatus(reportRequest.getReportStatus() != null ? reportRequest.getReportStatus() : ReportStatus.RECEIVED)
                .rejectedComment(reportRequest.getRejectedComment())
                .member(member)
                .build();

        Report savedReport = reportRepository.save(report);
        ReportResponse.from(savedReport);
    }

    //신고 수정하기
    @Transactional
    public void updateReport(int reportId, ReportRequest reportRequest) throws ReportException {
        Report report = reportRepository.findById(reportId);
        if(report == null){
            throw new ReportException(ReportErrorCode.REPORT_NOT_FOUND);
        }

        Member member = memberRepository.findByMemberId(reportRequest.getMemberId());
        if(member == null){
            throw new ReportException(ReportErrorCode.REPORT_NOT_FOUND_MEMBER);
        }

        report.update(
                reportRequest.getReportReason(),
                reportRequest.getReportStatus(),
                reportRequest.getRejectedComment(),
                member
        );
        ReportResponse.from(report);
    }

    //신고 삭제하기
    @Transactional
    public void deleteReport(int reportId) throws ReportException {
        if(!reportRepository.existsById(reportId)){
            throw new ReportException(ReportErrorCode.REPORT_NOT_FOUND);
        }
        reportRepository.deleteById(reportId);
    }
}
