package com.eneifour.fantry.checklist.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "checklist_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChecklistItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int checklistItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_template_id")
    private ChecklistTemplate checklistTemplate;

    private String itemKey;
    private String label;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(columnDefinition = "json")
    private String options;

    private Boolean required;
    private int orderIndex;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public enum Type { BOOL, SELECT, MULTISELECT, TEXT, NUMBER, GRADE, PHOTO }
}
