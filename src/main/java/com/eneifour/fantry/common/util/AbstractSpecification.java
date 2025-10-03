package com.eneifour.fantry.common.util;

import org.springframework.data.jpa.domain.Specification;

/**
 * Specification의 공통 구현을 캡슐화하여 동적 쿼리 작성을 돕는 추상 클래스입니다.
 * 이 클래스를 상속받는 하위 클래스는 구체적인 엔티티(T)와 검색 조건 DTO(C)를 지정하고,
 * toSpecification 메서드만 구현하면 재사용 가능한 Specification 로직을 쉽게 작성할 수 있습니다.
 *
 * @param <T> Specification을 적용할 엔티티 타입
 * @param <C> 검색 조건을 담고 있는 DTO 타입
 */
public abstract class AbstractSpecification<T, C> {

    /**
     * 검색 조건 DTO(condition)를 기반으로 최종 Specification 객체를 조립하는 추상 메서드입니다.
     * 하위 클래스는 이 메서드를 구현하여 도메인에 특화된 동적 쿼리 로직을 완성해야 합니다.
     *
     * @param condition 클라이언트로부터 받은 검색 조건들이 담긴 DTO
     * @return 모든 조건이 조합된 최종 Specification 객체
     */
    public abstract Specification<T> toSpecification(C condition);

    /**
     * 동적 쿼리 조립을 시작하기 위한 기본 Specification을 생성합니다.
     * 이 메서드는 SQL의 'WHERE 1=1'과 같이 항상 참(true)인 조건을 반환하므로,
     * 이후에 and() 메서드로 다른 조건들을 안전하게 연결할 수 있습니다.
     *
     * @return 항상 참(true)을 반환하는 기본 Specification
     */
    protected Specification<T> base() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    /**
     * 특정 필드에 대한 'equals' 비교 조건을 생성하는 헬퍼(helper) 메서드입니다.
     * 검색 조건 값이 null일 경우, 조건을 추가하지 않도록 null을 반환합니다.
     * (Specification.and() 메서드는 null 인자를 무시합니다.)
     *
     * @param fieldName 엔티티의 필드명 (e.g., "status")
     * @param value 비교할 값
     * @return 'equals' 조건의 Specification. 비교할 값이 null이면 null을 반환.
     */
    protected Specification<T> equal(String fieldName, Object value) {
        if (value == null) {
            return null;
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(fieldName), value);
    }

    /**
     * 특정 필드에 대한 'like' 비교 조건을 생성하는 헬퍼(helper) 메서드입니다.
     * 검색 조건 값이 null이거나 비어있을 경우, 조건을 추가하지 않도록 null을 반환합니다.
     *
     * @param fieldName 엔티티의 필드명 (e.g., "member.name")
     * @param value 비교할 값
     * @return 'like' 조건의 Specification. 비교할 값이 null이거나 비어있으면 null을 반환.
     */
    protected Specification<T> like(String fieldName, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        String[] fields = fieldName.split("\\.");
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(fields.length > 1 ? root.join(fields[0]).get(fields[1]) : root.get(fieldName), "%" + value + "%");
    }
}
