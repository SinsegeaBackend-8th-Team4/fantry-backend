package com.eneifour.fantry.cs.controller;


import com.eneifour.fantry.cs.dto.InquiryCreateRequest;
import com.eneifour.fantry.cs.dto.InquiryDetailUserResponse;
import com.eneifour.fantry.cs.dto.InquirySummaryResponse;
import com.eneifour.fantry.cs.service.InquiryService;
import com.eneifour.fantry.member.domain.Member;

import com.eneifour.fantry.member.repository.JpaMemberRepository;
import com.eneifour.fantry.security.dto.CustomUserDetails;
import com.eneifour.fantry.security.util.SecurityUtil;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cs/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;
    private final JpaMemberRepository memberRepository;

    /**
     * 문의 글(텍스트) 생성
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
