package com.eneifour.fantry.cs.domain;

import com.eneifour.fantry.common.util.file.FileMeta;
import com.eneifour.fantry.cs.exception.InquiryErrorCode;
import com.eneifour.fantry.cs.exception.InquiryException;
import com.eneifour.fantry.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Lob // 해당 필드는 대용략 텍스트임을 명시(스마트에디터)
    private String answerContent;

    @LastModifiedDate
    private LocalDateTime answeredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answered_by")
    private Member answeredBy;

    private String comment;

    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CsAttachment> attachments = new ArrayList<>();

    public void answer(String answerContent, String comment, Member admin) {
        if (answerContent == null || answerContent.isBlank()) {
            throw new InquiryException(InquiryErrorCode.ANSWER_CONTENT_IS_REQUIRED);
        }
        this.status = InquiryStatus.ANSWERED;
        this.answerContent = answerContent;
        this.comment = comment;
        this.answeredBy = admin;
    }

    public void putOnHold(String comment, Member admin) {
        this.status = InquiryStatus.ON_HOLD;
        this.comment = comment;
        this.answeredBy = admin;
        this.answerContent = null;
    }

    public void startProgress(String comment, Member admin) {
        this.status = InquiryStatus.IN_PROGRESS;
        this.comment = comment;
        this.answeredBy = admin;
        this.answerContent = null;
    }

    public void reject(String comment, Member admin) {
        this.status = InquiryStatus.REJECTED;
        this.comment = comment;
        this.answeredBy = admin;
        this.answerContent = null;
    }

    /**
     * 문의에 새로운 첨부파일을 추가합니다.
     * Cascade 옵션에 의해, 이 Inquiry가 저장될 때 Attachment도 함께 저장됩니다.
     * @param fileMeta 파일 메타데이터 엔티티
     */
    public void addAttachment(FileMeta fileMeta) {
        CsAttachment attachment = CsAttachment.builder()
                .inquiry(this) // 자기 자신(부모)을 명시적으로 연결
                .filemeta(fileMeta)
                .build();
        this.attachments.add(attachment);
    }
}
