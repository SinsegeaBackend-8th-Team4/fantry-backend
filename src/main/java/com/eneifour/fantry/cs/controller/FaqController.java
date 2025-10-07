package com.eneifour.fantry.cs.controller;

import com.eneifour.fantry.cs.dto.FaqResponse;
import com.eneifour.fantry.cs.dto.FaqSearchRequest;
import com.eneifour.fantry.cs.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cs/faq")
public class FaqController {

    private final FaqService faqService;

    /**
     * FAQ 목록을 조건에 따라 조회합니다. (페이징 포함)
     * @param request 검색 조건 (csType, title, content)
     * @param pageable 페이징 정보
     * @return FAQ 페이지 응답
     */
    @GetMapping
    public ResponseEntity<Page<FaqResponse>> searchFaqs(
            @ModelAttribute FaqSearchRequest request,
            Pageable pageable
    ) {
        Page<FaqResponse> results = faqService.searchFaqs(request, pageable);
        return ResponseEntity.ok(results);
    }
}
