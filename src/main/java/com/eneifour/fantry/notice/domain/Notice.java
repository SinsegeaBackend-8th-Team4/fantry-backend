package com.eneifour.fantry.notice.domain;

import com.eneifour.fantry.common.domain.BaseAuditingEntity;
import com.eneifour.fantry.common.util.file.FileMeta;
import com.eneifour.fantry.inquiry.domain.CsStatus;
import com.eneifour.fantry.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 공지사항 게시글을 나타내는 핵심 엔티티 클래스입니다.
 * BaseAuditingEntity를 상속받아 생성/수정 관련 필드를 자동으로 관리합니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
@Table(name="notice")
public class Notice extends BaseAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int noticeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_type_id")
    private NoticeType noticeType;

    private String title;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CsStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", updatable = false)
    private Member createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private Member updatedBy;

    @Builder.Default
    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoticeAttachment> attachments = new ArrayList<>();

    /**
     * 공지사항의 주요 정보를 수정합니다.
     * @param title 수정할 제목
     * @param content 수정할 내용 (HTML)
     * @param noticeType 수정할 카테고리
     * @param modifier 수정 작업을 수행하는 관리자
     */
    public void update(String title, String content, NoticeType noticeType, Member modifier) {
        this.title = title;
        this.content = content;
        this.noticeType = noticeType;
        this.updatedBy = modifier;
    }

    /**
     * 공지사항의 상태(공개/비공개 등)를 변경합니다.
     * @param status 새로운 상태
     */
    public void changeStatus(CsStatus status) {
        this.status = status;
    }

    /**
     * 이 공지사항에 새로운 첨부파일을 추가합니다.
     * @param fileMeta 파일 메타데이터 엔티티
     */
    public void addAttachment(FileMeta fileMeta) {
        NoticeAttachment attachment = NoticeAttachment.builder()
                .notice(this)
                .filemeta(fileMeta)
                .build();
        this.attachments.add(attachment);
    }

    /**
     * 이 공지사항에 연결된 모든 첨부파일 목록을 비웁니다.
     */
    public void clearAttachments() {
        this.attachments.clear();
    }
}
