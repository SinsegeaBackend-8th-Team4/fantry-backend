package com.eneifour.fantry.inspection.domain;

import com.eneifour.fantry.checklist.domain.ChecklistItemCategoryMap;
import com.eneifour.fantry.checklist.domain.ChecklistTemplate;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_checklist_answer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductChecklistAnswer {
    @EmbeddedId private Id id; // 복합키

    // product_inspection_id와 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productInspectionId")
    @JoinColumn(name = "product_inspection_id")
    private ProductInspection productInspection;

    private String itemLabel;

    @Column(columnDefinition = "json")
    private String answerValue;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // 복합키 클래스
    @Embeddable
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Id implements java.io.Serializable {
        private int productInspectionId;
        @Enumerated(EnumType.STRING)
        private ChecklistTemplate.Role checklistRole;
        private String itemKey;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Id)) return false;
            Id that = (Id) o;
            return productInspectionId == that.productInspectionId &&
                    checklistRole == that.checklistRole &&
                    java.util.Objects.equals(itemKey, that.itemKey);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(productInspectionId, checklistRole, itemKey);
        }
    }
}
