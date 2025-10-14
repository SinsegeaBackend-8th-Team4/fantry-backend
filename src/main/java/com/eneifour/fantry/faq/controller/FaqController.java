package com.eneifour.fantry.faq.controller;

import com.eneifour.fantry.faq.dto.FaqResponse;
import com.eneifour.fantry.faq.dto.FaqSearchRequest;
import com.eneifour.fantry.faq.service.FaqService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FAQ (User)", description = "사용자용 FAQ API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cs/faq")
public class FaqController {

    private final FaqService faqService;

    @Operation(summary = "FAQ 목록 동적 검색 (사용자)", description = "공개된 FAQ 목록을 검색합니다.")
    @Parameters({
            @Parameter(name = "csTypeId", description = "카테고리 ID (1:배송, 2:결제 등)", example = "1"),
            @Parameter(name = "keyword", description = "검색할 키워드", example = "배송"),
            @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
            @Parameter(name = "size", description = "페이지 당 항목 수", example = "10"),
            @Parameter(name = "sort", description = "정렬 조건 (예: faqId,desc)")
    })
    @GetMapping
    public ResponseEntity<Page<FaqResponse>> searchFaqs(
            @ModelAttribute FaqSearchRequest request,
            Pageable pageable
    ) {
        Page<FaqResponse> results = faqService.searchFaqsForUser(request, pageable);
        return ResponseEntity.ok(results);
    }
}
