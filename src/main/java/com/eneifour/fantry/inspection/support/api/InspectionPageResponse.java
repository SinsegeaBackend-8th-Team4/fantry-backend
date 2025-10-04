package com.eneifour.fantry.inspection.support.api;

import lombok.*;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class InspectionPageResponse<T> {
    private List<T> items;
    private Meta meta;

    @Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
    public static class Meta {
        private int page;          // 0-based
        private int size;
        private long totalElements;
        private int totalPages;
        private String sort;       // "createdAt,desc"
    }

    public static <T> InspectionApiResponse<InspectionPageResponse<T>> of(List<T> items, Meta meta) {
        return InspectionApiResponse.ok(InspectionPageResponse.<T>builder().items(items).meta(meta).build());
    }
}
