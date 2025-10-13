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
     * FAQ 목록을 동적 조건과 페이징을 적용하여 조회합니다.
     * 아코디언 UI를 위해, 응답 DTO에 질문(title)과 답변(content)을 모두 포함하여 반환합니다.
     *
     * @param request  검색 조건 (csType, title, content)
     * @param pageable 페이징 정보
     * @return 페이징된 FAQ 응답 DTO
     */
    public Page<FaqResponse> searchFaqs(FaqSearchRequest request, Pageable pageable) {
        Specification<Faq> spec = faqSpecification.toSpecification(request);
        Page<Faq> faqPage = faqRepository.findAll(spec, pageable);

        // Page 객체의 .map() 기능을 활용하여, Page<Faq>를 Page<FaqResponse>로 효율적으로 변환합니다.
        return faqPage.map(FaqResponse::from);
    }
}