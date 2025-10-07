package com.eneifour.fantry.cs.service;

import com.eneifour.fantry.cs.domain.Faq;
import com.eneifour.fantry.cs.dto.FaqResponse;
import com.eneifour.fantry.cs.dto.FaqSearchCondition;
import com.eneifour.fantry.cs.repository.FaqRepository;
import com.eneifour.fantry.cs.repository.FaqSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FaqService {
    private final FaqRepository faqRepository;
    private final FaqSpecification faqSpecification;

    public Page<FaqResponse> getPage(FaqSearchCondition condition, Pageable pageable){
        Specification<Faq> spec =faqSpecification.toSpecification(condition);
        Page<Faq> faqPage = faqRepository.findAll(spec, pageable);
        return faqPage.map(FaqResponse::from);
    }
}
