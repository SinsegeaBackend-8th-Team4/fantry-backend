package com.eneifour.fantry.inquiry.controller;

import com.eneifour.fantry.inquiry.dto.*;
import com.eneifour.fantry.inquiry.service.InquiryService;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.security.dto.CustomUserDetails;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "1:1 문의 API (관리자)", description = "관리자용 1:1 문의 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/cs/inquiry")
public class                              InquiryAdminController {

    private final InquiryService inquiryService;

    @Operation(summary = "1:1 문의 목록 조회 (검색)", description = "1:1 문의 목록을 검색 조건에 따라 페이징하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "목록 조회 성공", content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 또는 권한 없음")
    })
    @GetMapping
    public ResponseEntity<Page<InquirySummaryResponse>> searchInquiries(
            @Parameter(description = "검색 조건 (처리 상태, 문의 유형, 사용자 이름)") @ModelAttribute InquirySearchCondition condition,
            @Parameter(description = "페이징 정보 (page, size, sort)") Pageable pageable
    ) {
        Page<InquirySummaryResponse> results = inquiryService.searchInquires(condition, pageable);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "1:1 문의 상세 조회 (관리자)", description = "특정 1:1 문의의 상세 내용을 관리자용으로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상세 조회 성공", content = @Content(schema = @Schema(implementation = InquiryDetailAdminResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 또는 권한 없음"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 문의")
    })
    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryDetailAdminResponse> getInquiryDetail(
            @Parameter(description = "조회할 문의의 ID", required = true) @PathVariable int inquiryId
    ) {
        InquiryDetailAdminResponse inquiry = inquiryService.getInquiryForAdmin(inquiryId);
        return ResponseEntity.ok(inquiry);
    }

    @Operation(summary = "1:1 문의 답변 등록 및 상태 변경", description = "특정 1:1 문의에 답변을 등록하고 처리 상태를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "답변 등록 및 상태 변경 성공", content = @Content(schema = @Schema(implementation = InquiryDetailAdminResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 또는 권한 없음"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 문의")
    })
    @PatchMapping("/{inquiryId}/answer")
    public ResponseEntity<InquiryDetailAdminResponse> answerInquiry(
            @Parameter(description = "답변할 문의의 ID", required = true) @PathVariable int inquiryId,
            @Parameter(description = "답변 내용 및 변경할 상태 정보", required = true, schema = @Schema(implementation = InquiryAnswerRequest.class))
            @RequestBody InquiryAnswerRequest answerRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member admin = userDetails.getMember();
        InquiryDetailAdminResponse updatedInquiry = inquiryService.answerInquiry(inquiryId, answerRequest, admin);
        return ResponseEntity.ok(updatedInquiry);
    }

    @Operation(summary = "1:1 문의 통계 조회 (관리자)", description = "관리자 대시보드에 필요한 문의 통계 정보를 조회합니다. (상태별 개수)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "통계 조회 성공", content = @Content(schema = @Schema(implementation = InquiryStatsAdminResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 또는 권한 없음")
    })
    @GetMapping("/stats")
    public ResponseEntity<InquiryStatsAdminResponse> getInquiryStats() {
        InquiryStatsAdminResponse stats = inquiryService.getInquiryStatsForAdmin();
        return ResponseEntity.ok(stats);
    }
}
