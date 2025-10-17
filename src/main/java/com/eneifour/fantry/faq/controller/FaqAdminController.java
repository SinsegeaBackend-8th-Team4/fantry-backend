package com.eneifour.fantry.faq.controller;

import com.eneifour.fantry.faq.dto.*;
import com.eneifour.fantry.faq.service.FaqAdminService;
import com.eneifour.fantry.faq.service.FaqService;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "FAQ (Admin)", description = "관리자용 FAQ API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/cs/faq")
public class FaqAdminController {

    private final FaqAdminService faqAdminService;
    private final FaqService faqService;

    @Operation(summary = "신규 FAQ 등록", description = "새로운 FAQ를 시스템에 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "FAQ 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping
    public ResponseEntity<FaqResponse> createFaq(
            @RequestBody @Valid FaqCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member admin = userDetails.getMember();
        FaqResponse response = faqAdminService.createFaq(request, admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "FAQ 파일 첨부", description = "특정 FAQ에 파일을 첨부합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "파일 첨부 성공"),
            @ApiResponse(responseCode = "404", description = "FAQ를 찾을 수 없음")
    })
    @PostMapping(value = "/{faqId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addAttachments(
            @Parameter(description = "파일을 첨부할 FAQ의 ID") @PathVariable int faqId,
            @RequestParam("files") List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member admin = userDetails.getMember();
        faqAdminService.addAttachments(faqId, files, admin);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "FAQ 내용 수정", description = "특정 FAQ의 제목, 내용, 상태 등을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "FAQ 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "FAQ를 찾을 수 없음")
    })
    @PatchMapping("/{faqId}")
    public ResponseEntity<FaqResponse> updateFaq(
            @Parameter(description = "수정할 FAQ의 ID") @PathVariable int faqId,
            @RequestBody @Valid FaqUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member admin = userDetails.getMember();
        FaqResponse response = faqAdminService.updateFaq(faqId, request, admin);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "FAQ 삭제", description = "특정 FAQ를 시스템에서 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "FAQ 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "FAQ를 찾을 수 없음")
    })
    @DeleteMapping("/{faqId}")
    public ResponseEntity<Void> deleteFaq(
            @Parameter(description = "삭제할 FAQ의 ID") @PathVariable int faqId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Member admin = userDetails.getMember();
        faqAdminService.deleteFaq(faqId, admin);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "FAQ 목록 동적 검색 (관리자)", description = "다양한 조건으로 FAQ 목록을 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<FaqResponse>> searchFaqs(
            @Parameter(description = "검색 조건 (카테고리 ID, 키워드, 상태)") @ModelAttribute FaqSearchRequest request,
            @Parameter(description = "페이징 정보 (page, size, sort)") Pageable pageable
    ) {
        Page<FaqResponse> results = faqService.searchFaqsForAdmin(request, pageable);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "FAQ 상세 정보 조회", description = "특정 FAQ의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "FAQ를 찾을 수 없음")
    })
    @GetMapping("/{faqId}")
    public ResponseEntity<FaqResponse> getFaqDetail(@Parameter(description = "조회할 FAQ의 ID") @PathVariable int faqId) {
        FaqResponse faq = faqAdminService.getFaq(faqId);
        return ResponseEntity.ok(faq);
    }

    @Operation(summary = "FAQ 통계 조회 (관리자)", description = "관리자 대시보드에 필요한 FAQ 통계 정보를 조회합니다. (상태별 개수)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "통계 조회 성공", content = @Content(schema = @Schema(implementation = FaqStatsAdminResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 또는 권한 없음")
    })
    @GetMapping("/stats")
    public ResponseEntity<FaqStatsAdminResponse> getFaqStats() {
        FaqStatsAdminResponse stats = faqAdminService.getFaqStatsForAdmin();
        return ResponseEntity.ok(stats);
    }
}