package com.eneifour.fantry.cs.controller;

import com.eneifour.fantry.cs.dto.FaqResponse;
import com.eneifour.fantry.cs.dto.FaqSearchCondition;
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

    @GetMapping
    public ResponseEntity<Page<FaqResponse>> getFaqs(
        @ModelAttribute FaqSearchCondition condition,
                Pageable pageable
    ){
        Page<FaqResponse> faqPage = faqService.getPage(condition, pageable);
        return ResponseEntity.ok(faqPage);
    }
}
