package com.eneifour.fantry.cs.domain;

import com.eneifour.fantry.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notice")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int noticeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cs_type_id")
    private CsType csType;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private CsStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Member createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private Member updatedBy;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
