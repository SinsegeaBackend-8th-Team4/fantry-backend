package com.eneifour.fantry.faq.domain;

import com.eneifour.fantry.common.domain.BaseAuditingEntity;
import com.eneifour.fantry.common.util.file.FileMeta;
import com.eneifour.fantry.inquiry.domain.CsStatus;
import com.eneifour.fantry.inquiry.domain.CsType;
import com.eneifour.fantry.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
@Table(name="faq")
public class Faq extends BaseAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int faqId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cs_type_id")
    private CsType csType;

    private String title;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private CsStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", updatable = false)
    private Member createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private Member updatedBy;

    @Builder.Default
    @OneToMany(mappedBy = "faq", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FaqAttachment> attachments = new ArrayList<>();

    public void update(String title, String content, CsType csType, Member modifier) {
        this.title = title;
        this.content = content;
        this.csType = csType;
        this.updatedBy = modifier;
    }

    public void addAttachment(FileMeta fileMeta) {
        FaqAttachment attachment = FaqAttachment.builder()
                .faq(this)
                .filemeta(fileMeta)
                .build();
        this.attachments.add(attachment);
    }

    public void clearAttachments() {
        this.attachments.clear();
    }

    /**
     * FAQ의 상태(공개/비공개 등)를 변경합니다.
     */
    public void changeStatus(CsStatus status) {
        this.status = status;
    }
}
