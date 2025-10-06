package com.eneifour.fantry.report.dto;

import com.eneifour.fantry.report.domain.Report;
import com.eneifour.fantry.report.domain.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponse {
    private int reportId;
    private String reportReason;
    private String reportAt;
    private ReportStatus reportStatus;
    private String rejectedComment;
    private int memberId;

    public static ReportResponse from(Report report) {
        return ReportResponse.builder()
                .reportId(report.getReportId())
                .reportReason(report.getReportReason())
                .reportAt(report.getReportAt())
                .reportStatus(report.getReportStatus())
                .rejectedComment(report.getRejectedComment())
                .memberId(report.getMember().getMemberId())
                .build();
    }

    public static List<ReportResponse> fromList(List<Report> reports) {
        return reports.stream()
                .map(ReportResponse::from)
                .collect(Collectors.toList());
    }
}
