package com.eneifour.fantry.cs.controller;

import com.eneifour.fantry.cs.dto.InquiryAnswerRequest;
import com.eneifour.fantry.cs.dto.InquiryDetailAdminResponse;
import com.eneifour.fantry.cs.dto.InquirySearchCondition;
import com.eneifour.fantry.cs.dto.InquirySummaryResponse;
import com.eneifour.fantry.cs.service.InquiryService;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/cs/inquiry")
public class InquiryAdminController {

    private final InquiryService inquiryService;

    /**
     * 문의 목록 동적 검색
     * @param condition 검색 조건 (status, csType, memberName)
     * @param pageable 페이징 정보 (page, size, sort)
     * @return
     */
    @GetMapping
    public ResponseEntity<Page<InquirySummaryResponse>> searchInquiries(
            @ModelAttribute InquirySearchCondition condition,
            Pageable pageable
    ) {
        Page<InquirySummaryResponse> results = inquiryService.searchInquires(condition, pageable);
        return ResponseEntity.ok(results);
    }

    /**
     * 문의 상세 조회 (관리자용)
     * @param inquiryId 조회할 문의의 ID
     * @return 문의 상세 정보
     */
    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryDetailAdminResponse> getInquiryDetail(
            @PathVariable int inquiryId
    ) {
        InquiryDetailAdminResponse inquiry = inquiryService.getInquiryForAdmin(inquiryId);
        return ResponseEntity.ok(inquiry);
    }

    /**
     * 문의 답변 및 상태 변경
     * @param inquiryId 처리할 문의의 ID
     * @param answerRequest 답변 내용, 처리 상태, 관리자 코멘트
     * @param userDetails 현재 로그인한 관리자 정보
     * @return 처리된 문의 상세 정보
     */
    @PatchMapping("/{inquiryId}/answer")
    public ResponseEntity<InquiryDetailAdminResponse> answerInquiry(
            @PathVariable int inquiryId,
            @RequestBody InquiryAnswerRequest answerRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member admin = userDetails.getMember();
        InquiryDetailAdminResponse updatedInquiry = inquiryService.answerInquiry(inquiryId, answerRequest, admin);
        return ResponseEntity.ok(updatedInquiry);
    }
}
