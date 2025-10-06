package com.eneifour.fantry.report.dto;

import com.eneifour.fantry.report.domain.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequest {
    private String reportReason;
    private ReportStatus reportStatus;
    private String rejectedComment;
    private int memberId;
}
