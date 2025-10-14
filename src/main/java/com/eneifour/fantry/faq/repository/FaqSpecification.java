package com.eneifour.fantry.faq.repository;

import com.eneifour.fantry.common.util.AbstractSpecification;
import com.eneifour.fantry.faq.domain.Faq;
import com.eneifour.fantry.faq.dto.FaqSearchRequest;
import com.eneifour.fantry.inquiry.domain.CsStatus;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class FaqSpecification extends AbstractSpecification<Faq, FaqSearchRequest> {

    public Specification<Faq> toSpecification(FaqSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 카테고리 ID 조건
            if (request.csTypeId() != null) {
                predicates.add(cb.equal(root.get("csType").get("csTypeId"), request.csTypeId()));
            }

            // 키워드 조건 (제목 또는 내용)
            if (request.keyword() != null && !request.keyword().isBlank()) {
                predicates.add(cb.or(
                        cb.like(root.get("title"), "%" + request.keyword() + "%"),
                        cb.like(root.get("content"), "%" + request.keyword() + "%")
                ));
            }

            // 상태 조건 (관리자용)
            if (request.status() != null) {
                predicates.add(root.get("status").in(request.status()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * 사용자용 Specification을 생성합니다.
     * 항상 ACTIVE, PINNED 상태의 FAQ만 조회하도록 강제합니다.
     */
    public Specification<Faq> toUserSpecification(FaqSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 기본적으로 ACTIVE, PINNED 상태만 허용
            predicates.add(root.get("status").in(CsStatus.ACTIVE, CsStatus.PINNED));

            // 카테고리 ID 조건
            if (request.csTypeId() != null) {
                predicates.add(cb.equal(root.get("csType").get("csTypeId"), request.csTypeId()));
            }

            // 키워드 조건 (제목 또는 내용)
            if (request.keyword() != null && !request.keyword().isBlank()) {
                predicates.add(cb.or(
                        cb.like(root.get("title"), "%" + request.keyword() + "%"),
                        cb.like(root.get("content"), "%" + request.keyword() + "%")
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Specification<Faq> fetchJoins() {
        return (root, query, criteriaBuilder) -> {
            // getResultType()의 결과를 먼저 변수에 담아 null 체크를 수행한다.
            Class<?> resultType = Objects.requireNonNull(query).getResultType();
            if (resultType != null && resultType != Long.class && resultType != long.class) {
                root.fetch("csType", JoinType.LEFT);
                root.fetch("createdBy", JoinType.LEFT);
                root.fetch("updatedBy", JoinType.LEFT);
                query.distinct(true);
            }
            return criteriaBuilder.conjunction();
        };
    }

    private Specification<Faq> keywordSearch(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction();
            }
            String pattern = "%" + keyword + "%";
            // title 또는 content에 키워드가 포함되어 있으면 검색 (OR 조건)
            return cb.or(
                    cb.like(root.get("title"), pattern),
                    cb.like(root.get("content"), pattern)
            );
        };
    }
}