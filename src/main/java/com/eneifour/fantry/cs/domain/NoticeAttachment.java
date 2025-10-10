package com.eneifour.fantry.cs.domain;

import com.eneifour.fantry.common.util.file.FileMeta;
import jakarta.persistence.*;
import lombok.*;

/**
 * 공지사항과 첨부파일(FileMeta)을 연결하는 중간 엔티티입니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
@Table(name="notice_attachment")
public class NoticeAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int noticeAttachmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="notice_id")
    private Notice notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="filemeta_id")
    private FileMeta filemeta;
}
