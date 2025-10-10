package com.eneifour.fantry.report.domain;

import com.eneifour.fantry.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name="report")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private int reportId;

    @Column(name = "report_reason")
    private String reportReason;

    @Column(name = "report_at", insertable = false, updatable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private String reportAt;

    @Column(name = "report_status")
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

    @Column(name = "rejection_comment")
    private String rejectedComment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    public void update(String reportReason, ReportStatus reportStatus,
                       String rejectedComment, Member member) {
        this.reportReason = reportReason;
        this.reportStatus = reportStatus;
        this.rejectedComment = rejectedComment;
        this.member = member;
    }
}
