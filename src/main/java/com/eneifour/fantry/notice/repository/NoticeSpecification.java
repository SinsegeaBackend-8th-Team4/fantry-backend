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
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("noticeType");
                root.fetch("createdBy");
            }

            List<Predicate> predicates = new ArrayList<>();

            if (request.noticeTypeId() != null) {
                predicates.add(cb.equal(root.get("noticeType").get("noticeTypeId"), request.noticeTypeId()));
            }

            if (request.keyword() != null && !request.keyword().isBlank()) {
                predicates.add(cb.or(
                        cb.like(root.get("title"), "%" + request.keyword() + "%"),
                        cb.like(root.get("content"), "%" + request.keyword() + "%")
                ));
            }

            if (request.status() != null && !request.status().isEmpty()) {
                predicates.add(root.get("status").in(request.status()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<Notice> toUserSpecification(NoticeSearchRequest request) {
        return (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("noticeType");
            }

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(root.get("status").in(CsStatus.ACTIVE, CsStatus.PINNED));

            if (request.noticeTypeId() != null) {
                predicates.add(cb.equal(root.get("noticeType").get("noticeTypeId"), request.noticeTypeId()));
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