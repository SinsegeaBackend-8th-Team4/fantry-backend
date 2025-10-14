package com.eneifour.fantry.settlement.repository;

import com.eneifour.fantry.common.util.AbstractSpecification;
import com.eneifour.fantry.settlement.domain.Settlement;
import com.eneifour.fantry.settlement.dto.SettlementSearchCondition;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Settlement 엔티티에 대한 동적 쿼리를 생성합니다.
 */
@Component
public class SettlementSpecification extends AbstractSpecification<Settlement, SettlementSearchCondition> {

    @Override
    public Specification<Settlement> toSpecification(SettlementSearchCondition condition) {
        return base()
                .and(fetchJoins())
                .and(like("member.name", condition.getSellerName()))
                .and(equal("status", condition.getStatus()))
                .and(betweenRequestedAt(condition.getStartDate(), condition.getEndDate()));
    }

    private Specification<Settlement> fetchJoins() {
        return (root, query, criteriaBuilder) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("member", JoinType.LEFT);
                query.distinct(true);
            }
            return criteriaBuilder.conjunction();
        };
    }

    private Specification<Settlement> betweenRequestedAt(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (startDate != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("requestedAt"), startDate.atStartOfDay()));
            }
            if (endDate != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("requestedAt"), endDate.atTime(LocalTime.MAX)));
            }
            return predicate;
        };
    }
}
