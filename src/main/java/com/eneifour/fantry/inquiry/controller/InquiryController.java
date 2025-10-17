package com.eneifour.fantry.inquiry.controller;


import com.eneifour.fantry.inquiry.dto.InquiryCreateRequest;
import com.eneifour.fantry.inquiry.dto.InquiryDetailUserResponse;
import com.eneifour.fantry.inquiry.dto.InquirySummaryResponse;
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

@Tag(name = "1:1 문의 API (사용자)", description = "사용자용 1:1 문의 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cs/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;

    @Operation(summary = "1:1 문의 등록", description = "새로운 1:1 문의를 등록합니다. 문의 내용은 요청 본문에 담아 전송해야 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "문의 등록 성공", content = @Content(schema = @Schema(implementation = InquirySummaryResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @PostMapping
    public ResponseEntity<InquirySummaryResponse> createInquiry(
            @Parameter(description = "문의 생성에 필요한 데이터 (제목, 내용, 카테고리 ID). " +
                    "카테고리(csTypeId) ID: 1: 배송, 2: 결제, 3: 기타, 4: 상품, 5: 환불/반품, 6: 판매",
                    required = true, schema = @Schema(implementation = InquiryCreateRequest.class))
            @RequestBody @Valid InquiryCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Member member = userDetails.getMember();

        InquirySummaryResponse response = inquiryService.create(request, member);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "1:1 문의 파일 첨부", description = "특정 1:1 문의에 파일을 첨부합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "파일 첨부 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 문의")
    })
    @PostMapping(value = "/{inquiryId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addAttachments(
            @Parameter(description = "파일을 첨부할 문의의 ID", required = true) @PathVariable int inquiryId,
            @Parameter(description = "첨부할 파일 목록", required = true) @RequestParam ("files") List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails userDetails

    ){
        Member member = userDetails.getMember();

        inquiryService.addAttachments(inquiryId, files, member);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "나의 1:1 문의 목록 조회", description = "현재 로그인한 사용자가 작성한 1:1 문의 목록을 페이징하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "목록 조회 성공", content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @GetMapping
    public ResponseEntity<Page<InquirySummaryResponse>> getMyInquiries(
            @Parameter(description = "페이징 정보 (page, size, sort)") Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Member member = userDetails.getMember();
        log.warn("목록조회요청받음 : " + member);

        Page<InquirySummaryResponse> myInquiries = inquiryService.getMyInquiries(member, pageable);
        return ResponseEntity.ok(myInquiries);
    }

    @Operation(summary = "나의 1:1 문의 상세 조회", description = "나의 특정 1:1 문의 상세 내용을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상세 조회 성공", content = @Content(schema = @Schema(implementation = InquiryDetailUserResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 문의")
    })
    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryDetailUserResponse> getMyInquiryDetail(
            @Parameter(description = "조회할 문의의 ID", required = true) @PathVariable int inquiryId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member member = userDetails.getMember();

        InquiryDetailUserResponse response = inquiryService.getMyInquiry(inquiryId, member);
        return ResponseEntity.ok(response);
    }


}
