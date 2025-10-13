package com.eneifour.fantry.refund.domain;

import com.eneifour.fantry.common.util.file.FileMeta;
import jakarta.persistence.*;
import lombok.*;

/**
 * 환불/반품 요청에 첨부된 파일 정보를 나타내는 엔티티입니다.
 * ReturnRequest와 FileMeta 사이의 관계를 매핑하는 연결 테이블 역할을 합니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
@Table(name="return_attachment")
public class ReturnAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int returnAttachmentId;

    // 이 첨부파일이 속한 환불/반품 요청
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="return_request_id")
    private ReturnRequest returnRequest;

    // 첨부된 파일의 메타 정보 (S3 경로, 파일명 등)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="filemeta_id")
    private FileMeta filemeta;
}
