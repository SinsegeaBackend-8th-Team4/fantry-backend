package com.eneifour.fantry.notice.repository;

import com.eneifour.fantry.common.util.AbstractSpecification;
import com.eneifour.fantry.notice.domain.Notice;
import com.eneifour.fantry.notice.dto.NoticeSearchRequest;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Notice 엔티티에 대한 동적 쿼리(Specification)를 생성합니다.
 */
@Component
public class NoticeSpecification extends AbstractSpecification<Notice, NoticeSearchRequest> {

    @Override
    public Specification<Notice> toSpecification(NoticeSearchRequest condition) {
        return base()
                .and(fetchJoins())
                .and(equal("csType.csTypeId", condition.csTypeId()))
                .and(equal("status", condition.status()))
                .and(keywordSearch(condition.keyword()));
    }

    private Specification<Notice> fetchJoins() {
        return (root, query, cb) -> {
            Class<?> resultType = Objects.requireNonNull(query).getResultType();
            if (resultType != null && resultType != Long.class && resultType != long.class) {
                root.fetch("csType", JoinType.LEFT);
                root.fetch("createdBy", JoinType.LEFT);
                root.fetch("updatedBy", JoinType.LEFT);
                query.distinct(true);
            }
            return cb.conjunction();
        };
    }

    private Specification<Notice> keywordSearch(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction();
            }
            String pattern = "%" + keyword + "%";
            return cb.or(
                    cb.like(root.get("title"), pattern),
                    cb.like(root.get("content"), pattern)
            );
        };
    }
}
