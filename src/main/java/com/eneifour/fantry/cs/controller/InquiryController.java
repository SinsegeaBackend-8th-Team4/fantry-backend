package com.eneifour.fantry.cs.controller;


import com.eneifour.fantry.cs.dto.InquiryCreateRequest;
import com.eneifour.fantry.cs.dto.InquiryDetailUserResponse;
import com.eneifour.fantry.cs.dto.InquirySummaryResponse;
import com.eneifour.fantry.cs.service.InquiryService;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.security.dto.CustomUserDetails;
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

/**
 * 사용자용 1:1 문의 관련 API 엔드포인트를 정의하는 컨트롤러입니다.
 * 모든 API는 인증된 사용자만 접근할 수 있습니다.
 *
 * @author 정재환
 * @since 2025.10.11
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cs/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;

    /**
     * 사용자가 새로운 1:1 문의를 생성합니다.
     * 성공 시 생성된 문의의 요약 정보와 함께 201 Created 상태 코드를 반환합니다.
     *
     * @param request     문의 생성에 필요한 데이터 DTO
     * @param userDetails 현재 인증된 사용자의 정보
     * @return 생성된 문의의 요약 정보가 담긴 ResponseEntity
     */
    @PostMapping
    public ResponseEntity<InquirySummaryResponse> createInquiry(
            @RequestBody @Valid InquiryCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Member member = userDetails.getMember();

        InquirySummaryResponse response = inquiryService.create(request, member);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 문의에 파일 첨부
     * @param inquiryId
     * @param files
     * @param userDetails
     * @return
     */
    @PostMapping(value = "/{inquiryId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addAttachments(
            @PathVariable int inquiryId,
            @RequestParam ("files") List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails userDetails

    ){
        Member member = userDetails.getMember();

        inquiryService.addAttachments(inquiryId, files, member);
        return ResponseEntity.ok().build();
    }

    /**
     * 나의 문의 목록 조회 (페이징)
     */
    @GetMapping
    public ResponseEntity<Page<InquirySummaryResponse>> getMyInquiries(
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Member member = userDetails.getMember();
        log.warn("목록조회요청받음 : " + member);

        Page<InquirySummaryResponse> myInquiries = inquiryService.getMyInquiries(member, pageable);
        return ResponseEntity.ok(myInquiries);
    }

    /**
     * 나의 문의 상세 조회
     */
    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryDetailUserResponse> getMyInquiryDetail(
            @PathVariable int inquiryId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member member = userDetails.getMember();

        InquiryDetailUserResponse response = inquiryService.getMyInquiry(inquiryId, member);
        return ResponseEntity.ok(response);
    }


}
