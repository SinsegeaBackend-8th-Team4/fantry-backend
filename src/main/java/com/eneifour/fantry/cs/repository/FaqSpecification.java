package com.eneifour.fantry.cs.repository;

import com.eneifour.fantry.common.util.AbstractSpecification;
import com.eneifour.fantry.cs.domain.Faq;
import com.eneifour.fantry.cs.domain.Inquiry;
import com.eneifour.fantry.cs.dto.FaqSearchCondition;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * Faq 엔티티에 대한 동적 쿼리 생서
 */
@Component
public class FaqSpecification extends AbstractSpecification<Faq, FaqSearchCondition> {

    @Override
    public Specification<Faq> toSpecification(FaqSearchCondition condition) {

        Specification<Faq> spec = base()
                .and(like("question", condition.getQuestion()))
                .and(like("answer", condition.getAnswer()))
                .and(equal("csType", condition.getCsType()));
        return spec;
    }

}
