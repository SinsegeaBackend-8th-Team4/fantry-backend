package com.eneifour.fantry.cs.domain;

import com.eneifour.fantry.common.domain.BaseAuditingEntity;
import com.eneifour.fantry.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="faq")
public class Faq extends BaseAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int faqId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cs_type_id")
    private CsType csType;

    private String question;

    private String answer;

    @Enumerated(EnumType.STRING)
    private CsStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
