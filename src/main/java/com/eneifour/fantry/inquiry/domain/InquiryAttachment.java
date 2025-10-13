package com.eneifour.fantry.inquiry.domain;

import com.eneifour.fantry.common.util.file.FileMeta;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="inquiry_attachment")
public class InquiryAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inquiryAttachmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="inquiry_id")
    private Inquiry inquiry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="filemeta_id")
    private FileMeta filemeta;
}
