package com.eneifour.fantry.refund.service;

import com.eneifour.fantry.common.util.AbstractSpecification;
import com.eneifour.fantry.refund.domain.ReturnRequest;
import com.eneifour.fantry.refund.dto.ReturnSearchRequest;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * ReturnRequest 엔티티에 대한 동적 쿼리를 생성합니다.
 */
@Component
public class ReturnSpecification extends AbstractSpecification<ReturnRequest, ReturnSearchRequest> {

    @Override
    public Specification<ReturnRequest> toSpecification(ReturnSearchRequest condition) {
        return base()
                .and(fetchJoins())
                .and(equal("status", condition.status()))
                .and(like("member.name", condition.buyerName()));
    }

    private Specification<ReturnRequest> fetchJoins() {
        return (root, query, cb) -> {
            Class<?> resultType = Objects.requireNonNull(query).getResultType();
            if (resultType != null && resultType != Long.class && resultType != long.class) {
                root.fetch("orders", JoinType.LEFT);
                root.fetch("member", JoinType.LEFT);
            }
            return cb.conjunction();
        };
    }
}
