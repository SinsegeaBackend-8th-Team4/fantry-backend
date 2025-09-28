package com.eneifour.fantry.cs.controller.cs.domain;

import com.eneifour.fantry.member.domain.Member;
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
    private int csAttachmentId; // PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="inquiry_id",referencedColumnName = "inquiry_id", nullable = true)
    private Member inquiryId; // FK 업로드한 멤버s

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="filemeta_id",referencedColumnName = "filemeta_id", nullable = true)
    private Member filemetaId;
}
