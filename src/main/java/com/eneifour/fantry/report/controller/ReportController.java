package com.eneifour.fantry.report.controller;

import com.eneifour.fantry.report.dto.ReportRequest;
import com.eneifour.fantry.report.dto.ReportResponse;
import com.eneifour.fantry.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {
    private final ReportService reportService;

    //모든 신고 내역 가져오기
    @GetMapping("/")
    public ResponseEntity<?> getReports() {
        List<ReportResponse> reportList = reportService.getReports();
        return ResponseEntity.ok().body(Map.of("reportList",  reportList));
    }

    //한명의 회원에 대한 모든 신고 내역 가져오기
    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getReportMember(@PathVariable int memberId) {
        List<ReportResponse> myReportList = reportService.getReportsByMemberId(memberId);
        return ResponseEntity.ok().body(Map.of("reportList",  myReportList));
    }

    //구제 신청 상태의 모든 신고 내역 가져오기
    @GetMapping("/received")
    public ResponseEntity<?> getReceivedReports() {
        List<ReportResponse> myReportList = reportService.getReceivedReports();
        return ResponseEntity.ok().body(Map.of("reportList",  myReportList));
    }

    //하나의 신고 내역 가져오기
    @GetMapping("/{reportId}")
    public ResponseEntity<?> getReport(@PathVariable int reportId) {
        ReportResponse report = reportService.getReport(reportId);
        return ResponseEntity.ok().body(Map.of("report",report));
    }

    //신고 추가하기
    @PostMapping("/")
    public ResponseEntity<?> createReport(@RequestBody ReportRequest report) {
        reportService.saveReport(report);
        return ResponseEntity.ok().body(Map.of("result", "신고가 성공적으로 등록됨"));
    }

    //신고 수정하기
    @PutMapping("/{reportId}")
    public ResponseEntity<?> updateReport(@PathVariable int reportId, @RequestBody ReportRequest report) {
        reportService.updateReport(reportId, report);
        return ResponseEntity.ok().body(Map.of("result", "신고 내역이 성공적으로 수정됨"));
    }

    //신고 삭제하기
    @DeleteMapping("/{reportId}")
    public ResponseEntity<?> deleteReport(@PathVariable int reportId) {
        reportService.deleteReport(reportId);
        return ResponseEntity.ok().body(Map.of("result", "신고 내역이 성공적으로 삭제됨"));
    }
}
