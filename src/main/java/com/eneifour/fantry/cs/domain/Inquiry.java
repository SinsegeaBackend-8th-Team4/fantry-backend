package com.eneifour.fantry.cs.domain;

import com.eneifour.fantry.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Builder
@Table(name="inquiry")
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inquiryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cs_type_id")
    private CsType csType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquired_by")
    private Member inquiredBy;

    private String title;
    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime inquiredAt;

    @Enumerated(EnumType.STRING)
    private InquiryStatus status;

    private String answerContent;

    @LastModifiedDate
    private LocalDateTime answeredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answered_by")
    private Member answeredBy;

    private String comment;

    public void addAnswer(String answerContent, Member answeredBy, String comment) {
        this.answerContent = answerContent;
        this.answeredBy = answeredBy;
        this.comment = comment;
        this.status = InquiryStatus.ANSWERED;
    }
}
