package com.eneifour.fantry.inquiry.repository;

import com.eneifour.fantry.common.util.AbstractSpecification;
import com.eneifour.fantry.inquiry.domain.Inquiry;
import com.eneifour.fantry.inquiry.dto.InquirySearchCondition;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * Inquiry 엔티티에 대한 동적 쿼리를 생성
 */
@Component
public class InquirySpecification extends AbstractSpecification<Inquiry, InquirySearchCondition> {

    /**
     * 검색 조건 DTO(InquirySearchCondition)를 기반으로 최종 Specification 객체를 조립.
     * @param condition 클라이언트로부터 받은 검색 조건 객체(집합)
     * @return 조립된 최종 Specification 객체
     */
    @Override
    public Specification<Inquiry> toSpecification(InquirySearchCondition condition) {
        // 기본 Specification (WHERE 1=1) 에서 시작
        Specification<Inquiry> spec = base()
                // N+1 문제 해결을 위한 fetch join 적용
                .and(fetchJoins())
                // 조건 DTO의 status 필드가 null이 아니면, status 검색 조건을 추가
                .and(equal("status", condition.getStatus()))
                // 조건 DTO의 csTypeId 필드가 null이 아니면, csTypeId 검색 조건을 추가
                .and(equalCsTypeId(condition.getCsTypeId()))
                // 조건 DTO의 memberName 필드가 null이 아니면, member.name like 검색 조건을 추가
                .and(like("inquiredBy.name", condition.getMemberName()));

        return spec;
    }

    private Specification<Inquiry> equalCsTypeId(Integer csTypeId) {
        return (root, query, criteriaBuilder) -> {
            if (csTypeId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("csType").get("csTypeId"), csTypeId);
        };
    }

    /**
     * N+1 문제를 방지하기 위해 연관된 엔티티(createdBy, answeredBy)를 fetch join 수행
     * Pageable을 사용한 페이징 쿼리 시, JPA는 데이터 조회 쿼리와 별개로 count 쿼리를 수행함.
     * count 쿼리에는 fetch join이 필요 없으며, 오히려 성능을 저하시키거나 에러를 유발할 수 있으므로
     * 현재 실행되는 쿼리가 count 쿼리가 아닐 경우에만 fetch join을 적용하도록 분기 처리합니다.
     * </p>
     * @return Fetch join이 적용된 Specification 객체
     */
    private Specification<Inquiry> fetchJoins() {
        return (root, query, criteriaBuilder) -> {
            // 현재 쿼리의 반환 타입을 확인하여 count 쿼리인지 판별
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                // ToOne 관계인 inquiredBy와 answeredBy를 LEFT JOIN FETCH 함
                root.fetch("inquiredBy", JoinType.LEFT);
                root.fetch("answeredBy", JoinType.LEFT);
                root.fetch("csType", JoinType.LEFT);
                // Fetch Join으로 인해 발생할 수 있는 데이터 중복을 제거
                query.distinct(true);
            }
            // 어떤 경우에도 쿼리 조건에 영향을 주지 않도록 항상 참(true)을 반환
            return criteriaBuilder.conjunction();
        };
    }
}
