package com.eneifour.fantry.cs.controller;

import com.eneifour.fantry.cs.dto.InquirySearchCondition;
import com.eneifour.fantry.cs.dto.InquirySummaryResponse;
import com.eneifour.fantry.cs.service.InquiryService;
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
@RequestMapping("/api/admin/cs")
public class InquiryAdminController {

    private final InquiryService inquiryService;

    /**
     * 문의 목록 동적 검색
     * @param condition 검색 조건 (status, csType, memberName)
     * @param pageable 페이징 정보 (page, size, sort)
     * @return
     */
    @GetMapping(value="/inquiries")
    public ResponseEntity<Page<InquirySummaryResponse>> searchInquiries(
            @ModelAttribute InquirySearchCondition condition,
            Pageable pageable
    ) {
        Page<InquirySummaryResponse> results = inquiryService.searchInquires(condition, pageable);
        return ResponseEntity.ok(results);
    }

}
