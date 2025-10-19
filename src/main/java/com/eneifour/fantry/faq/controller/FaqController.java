package com.eneifour.fantry.faq.controller;

import com.eneifour.fantry.faq.dto.FaqResponse;
import com.eneifour.fantry.faq.dto.FaqSearchRequest;
import com.eneifour.fantry.faq.service.FaqService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FAQ API (사용자)", description = "사용자용 FAQ 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cs/faq")
public class FaqController {

    private final FaqService faqService;

    @Operation(summary = "FAQ 목록 조회 (검색)", description = "공개된 FAQ 목록을 검색 조건에 따라 페이징하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<FaqResponse>> searchFaqs(
            @Parameter(description = "검색 조건 (카테고리 ID, 키워드). 'status' 필드는 무시됩니다.") @ModelAttribute FaqSearchRequest request,
            @Parameter(description = "페이징 정보 (page, size, sort)") Pageable pageable
    ) {
        Page<FaqResponse> results = faqService.searchFaqsForUser(request, pageable);
        return ResponseEntity.ok(results);
    }
}
