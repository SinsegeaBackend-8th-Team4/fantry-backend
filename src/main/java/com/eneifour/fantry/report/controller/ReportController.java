package com.eneifour.fantry.report.controller;

import com.eneifour.fantry.report.domain.Report;
import com.eneifour.fantry.report.model.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportController {
    private final ReportService reportService;

    //모든 신고 내역 가져오기
    @GetMapping("/report")
    public ResponseEntity<?> getReports() {
        List<Report> reportList = reportService.getReports();
        return ResponseEntity.ok().body(Map.of("reportList",  reportList));
    }

    //한명의 회원에 대한 모든 신고 내역 가져오기
    @GetMapping("/report/member/{memberId}")
    public ResponseEntity<?> getReportMember(@PathVariable int memberId) {
        List<Report> myReportList = reportService.getReportsByMemberId(memberId);
        return ResponseEntity.ok().body(Map.of("reportList",  myReportList));
    }

    //하나의 신고 내역 가져오기
    @GetMapping("/report/{reportId}")
    public ResponseEntity<?> getReport(@PathVariable int reportId) {
        Report report = reportService.getReport(reportId);
        return ResponseEntity.ok().body(Map.of("report",report));
    }

    //신고 추가하기
    @PostMapping("/report")
    public ResponseEntity<?> createReport(@RequestBody Report report) {
        reportService.saveReport(report);
        return ResponseEntity.ok().body(Map.of("result", "신고가 성공적으로 등록됨"));
    }

    //신고 수정하기
    @PutMapping("/report/{reportId}")
    public ResponseEntity<?> updateReport(@PathVariable int reportId, @RequestBody Report report) {
        reportService.updateReport(report);
        return ResponseEntity.ok().body(Map.of("result", "신고 내역이 성공적으로 수정됨"));
    }

    //신고 삭제하기
    @DeleteMapping("/report/{reportId}")
    public ResponseEntity<?> deleteReport(@PathVariable int reportId) {
        reportService.deleteReport(reportId);
        return ResponseEntity.ok().body(Map.of("result", "신고 내역이 성공적으로 삭제됨"));
    }
}
