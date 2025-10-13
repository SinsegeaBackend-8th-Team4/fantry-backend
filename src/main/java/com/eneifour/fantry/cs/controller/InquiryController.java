package com.eneifour.fantry.cs.controller;


import com.eneifour.fantry.cs.domain.Inquiry;
import com.eneifour.fantry.cs.dto.InquiryCreateRequest;
import com.eneifour.fantry.cs.dto.InquiryDetailResponse;
import com.eneifour.fantry.cs.dto.InquirySummaryResponse;
import com.eneifour.fantry.cs.exception.CsErrorCode;
import com.eneifour.fantry.cs.exception.CsException;
import com.eneifour.fantry.cs.repository.InquiryRepository;
import com.eneifour.fantry.cs.service.InquiryService;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.model.JpaMemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cs/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;
    private final JpaMemberRepository memberRepository;
    private final InquiryRepository inquiryRepository; // 소유권 확인을 위해 추가

    /**
     * 문의 글(텍스트) 생성
     */
    @PostMapping
    public ResponseEntity<InquirySummaryResponse> createInquiry(
            @RequestBody @Valid InquiryCreateRequest request
            // @AuthenticationPrincipal UserDetailsImpl userDetails // 실제 연동 시 사용
    ){
        // 임시멤버 생성, TODO : 로그인 기능 구현 완료시, 실제 로그인한 멤버로 가져오기
        Member tempMember = memberRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        InquirySummaryResponse response = inquiryService.create(request, tempMember);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 문의에 파일 첨부
     */
    @PostMapping(value = "/{inquiryId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addAttachments(
            @PathVariable int inquiryId,
            @RequestParam ("files") List<MultipartFile> files
            // @AuthenticationPrincipal UserDetailsImpl userDetails // 실제 연동 시 사용
    ){
        // 임시멤버 생성, TODO : 로그인 기능 구현 완료시, 실제 로그인한 멤버로 가져오기
        Member tempMember = memberRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
                inquiryService.addAttachments(inquiryId, files, tempMember);
                return ResponseEntity.ok().build();
    }

    /**
     * 나의 문의 목록 조회 (페이징)
     */
    @GetMapping
    public ResponseEntity<Page<InquirySummaryResponse>> getMyInquiries(
            Pageable pageable
//            @AuthenticationPrincipal UserDetails userDetails
    ){
        Member tempMember = memberRepository.findById(1).orElseThrow(); // 테스트용 임시 멤버

        Page<InquirySummaryResponse> myInquiries = inquiryService.findMyInquiries(tempMember, pageable);
        return ResponseEntity.ok(myInquiries);
    }

    /**
     * 나의 문의 상세 조회
     */
    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryDetailResponse> getMyInquiryDetail(@PathVariable int inquiryId) {
        // 임시멤버 생성, TODO : 로그인 기능 구현 완료시, 실제 로그인한 멤버로 가져오기
        Member tempMember = memberRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // 서비스 호출 전, 컨트롤러에서 소유권 확인
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new CsException(CsErrorCode.INQUIRY_NOT_FOUND));

        if (inquiry.getInquiredBy().getMemberId() != tempMember.getMemberId()) {
            throw new CsException(CsErrorCode.ACCESS_DENIED);
        }

        InquiryDetailResponse response = inquiryService.getInquiry(inquiryId);
        return ResponseEntity.ok(response);
    }
}
