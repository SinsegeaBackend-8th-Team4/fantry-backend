package com.eneifour.fantry.faq.domain;

import com.eneifour.fantry.common.util.file.FileMeta;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="faq_attachment")
public class FaqAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int faqAttachmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="faq_id")
    private Faq faq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="filemeta_id")
    private FileMeta filemeta;
}
