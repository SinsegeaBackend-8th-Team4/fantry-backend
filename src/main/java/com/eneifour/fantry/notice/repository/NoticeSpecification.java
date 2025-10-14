package com.eneifour.fantry.notice.repository;

import com.eneifour.fantry.inquiry.domain.CsStatus;
import com.eneifour.fantry.notice.domain.Notice;
import com.eneifour.fantry.notice.dto.NoticeSearchRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NoticeSpecification {

    public Specification<Notice> toSpecification(NoticeSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.csTypeId() != null) {
                predicates.add(cb.equal(root.get("csType").get("csTypeId"), request.csTypeId()));
            }

            if (request.keyword() != null && !request.keyword().isBlank()) {
                predicates.add(cb.or(
                        cb.like(root.get("title"), "%" + request.keyword() + "%"),
                        cb.like(root.get("content"), "%" + request.keyword() + "%")
                ));
            }

            if (request.status() != null) {
                predicates.add(root.get("status").in(request.status()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<Notice> toUserSpecification(NoticeSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(root.get("status").in(CsStatus.ACTIVE, CsStatus.PINNED));

            if (request.csTypeId() != null) {
                predicates.add(cb.equal(root.get("csType").get("csTypeId"), request.csTypeId()));
            }

            if (request.keyword() != null && !request.keyword().isBlank()) {
                predicates.add(cb.or(
                        cb.like(root.get("title"), "%" + request.keyword() + "%"),
                        cb.like(root.get("content"), "%" + request.keyword() + "%")
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}