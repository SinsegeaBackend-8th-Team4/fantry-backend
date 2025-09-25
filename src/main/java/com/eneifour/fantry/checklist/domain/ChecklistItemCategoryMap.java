package com.eneifour.fantry.checklist.domain;

import com.eneifour.fantry.catalog.domain.GoodsCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "checklist_item_category_map")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChecklistItemCategoryMap {
    @EmbeddedId private Id id; // 복합키

    // checklist_item_id와 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("checklistItemId")
    @JoinColumn(name = "checklist_item_id")
    private ChecklistItem checklistItem;

    // goods_category_id와 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("goodsCategoryId")
    @JoinColumn(name = "goods_category_id")
    private GoodsCategory goodsCategory;

    // 복합키 클래스
    @Embeddable
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Id implements java.io.Serializable {
        private int checklistItemId;
        private int goodsCategoryId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Id)) return false;
            Id that = (Id) o;
            return checklistItemId == that.checklistItemId &&
                    goodsCategoryId == that.goodsCategoryId;
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(checklistItemId, goodsCategoryId);
        }
    }

}
