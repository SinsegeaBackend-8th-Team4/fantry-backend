package com.eneifour.fantry.checklist.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "checklist_template")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChecklistTemplate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int checklistTemplateId;

    private String code;
    private String title;
    private int version;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime published_at;

    @Column(insertable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(insertable = false, updatable = false)
    private LocalDateTime updated_at;

    public enum Role { SELLER, INSPECTOR }
    public enum Status { DRAFT, PUBLISHED, ARCHIVED }
}
