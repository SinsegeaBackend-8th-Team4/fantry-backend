package com.eneifour.fantry.cs.domain;

import com.eneifour.fantry.common.util.file.FileMeta;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="cs_attachment")
public class CsAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int csAttachmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="inquiry_id")
    private Inquiry inquiry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="filemeta_id")
    private FileMeta filemeta;
}
