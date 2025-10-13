package com.eneifour.fantry.faq.controller;

import com.eneifour.fantry.faq.dto.FaqResponse;
import com.eneifour.fantry.faq.dto.FaqSearchRequest;
import com.eneifour.fantry.faq.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FAQ(자주 묻는 질문) 관련 API를 제공하는 컨트롤러입니다.
 *
 * @author 정재환
 * @since 2025.10.11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cs/faq")
public class FaqController {

    private final FaqService faqService;

    /**
     * FAQ 목록을 검색 조건에 따라 페이징하여 조회합니다.
     * <p>검색 조건(카테고리, 키워드)은 선택 사항이며, 조건이 없으면 'ACTIVE' 또는 'PINNED' 상태의 전체 목록이 조회됩니다.
     *
     * @param request  검색 조건 DTO.
     *                 <p><b>[검색 가능한 상태(status)]</b>: 사용자 API에서는 기본적으로 'ACTIVE'와 'PINNED'만 조회됩니다.</p>
     *                  <ul>
     *                      <li>ACTIVE: 활성 (노출)</li>
     *                      <li>PINNED: 상단 고정</li>
     *                  </ul>
     *                  <p><b>[카테고리(csTypeId) ID]</b></p>
     *                  <ul>
     *                      <li>1: 배송문의</li>
     *                      <li>2: 결제문의</li>
     *                      <li>3: 기타문의</li>
     *                      <li>4: 상품문의</li>
     *                      <li>5: 환불/반품 문의</li>
     *                      <li>6: 판매 문의</li>
     *                  </ul>
     * @param pageable 페이징 정보 (페이지 번호, 페이지 크기, 정렬).
     * @return 페이징 처리된 FAQ 목록.
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
