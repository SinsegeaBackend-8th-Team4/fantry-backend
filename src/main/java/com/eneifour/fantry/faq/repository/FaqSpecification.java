package com.eneifour.fantry.faq.repository;

import com.eneifour.fantry.common.util.AbstractSpecification;
import com.eneifour.fantry.faq.domain.Faq;
import com.eneifour.fantry.faq.dto.FaqSearchRequest;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FaqSpecification extends AbstractSpecification<Faq, FaqSearchRequest> {

    @Override
    public Specification<Faq> toSpecification(FaqSearchRequest condition) {
        return base()
                .and(fetchJoins())
                .and(equal("csType.csTypeId", condition.csTypeId()))
                .and(equal("status", condition.status()))
                .and(keywordSearch(condition.keyword()));
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