package com.eneifour.fantry.inspection.support.api;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * FE가 테이블/페이지네이션에서 공통으로 쓰는 표준 페이지 응답 형태
 * - items: 실제 데이터 배열
 */
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
    }

    /** Spring Data Page <T> -> 표준 페이로드로 변환 */
    public static <T> InspectionPageResponse<T> fromPage(Page<T> page) {
        return InspectionPageResponse.<T>builder()
                .items(page.getContent())                       // 실제 리스트
                .meta(Meta.builder()
                        .page(page.getNumber())                 // 현재 페이지
                        .size(page.getSize())                   // 요청 size
                        .totalElements(page.getTotalElements()) // 총 행 수 
                        .totalPages(page.getTotalPages())       // 총 페이지 수
                        .build())
                .build();
    }
}
