package com.eneifour.fantry.faq.service;

import com.eneifour.fantry.faq.domain.Faq;
import com.eneifour.fantry.faq.dto.FaqResponse;
import com.eneifour.fantry.faq.dto.FaqSearchRequest;
import com.eneifour.fantry.faq.repository.FaqRepository;
import com.eneifour.fantry.faq.repository.FaqSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자용 FAQ 조회 서비스를 담당합니다.
 * 이 서비스는 데이터 변경(C,U,D) 로직을 포함하지 않는 읽기 전용 서비스입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqService {

    private final FaqRepository faqRepository;
    private final FaqSpecification faqSpecification;

    /**
     * (관리자용) FAQ 목록을 동적 조건과 페이징을 적용하여 조회합니다.
     */
    public Page<FaqResponse> searchFaqsForAdmin(FaqSearchRequest request, Pageable pageable) {
        Specification<Faq> spec = faqSpecification.toSpecification(request);
        Page<Faq> faqPage = faqRepository.findAll(spec, pageable);

        return faqPage.map(FaqResponse::from);
    }

    /**
     * (사용자용) FAQ 목록을 동적 조건과 페이징을 적용하여 조회합니다.
     * 공개된 상태(ACTIVE, PINNED)의 FAQ만 조회합니다.
     */
    public Page<FaqResponse> searchFaqsForUser(FaqSearchRequest request, Pageable pageable) {
        Specification<Faq> spec = faqSpecification.toUserSpecification(request);
        Page<Faq> faqPage = faqRepository.findAll(spec, pageable);

        return faqPage.map(FaqResponse::from);
    }
}