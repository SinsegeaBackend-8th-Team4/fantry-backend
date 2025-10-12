package com.eneifour.fantry.inquiry.controller;


import com.eneifour.fantry.inquiry.dto.InquiryCreateRequest;
import com.eneifour.fantry.inquiry.dto.InquiryDetailUserResponse;
import com.eneifour.fantry.inquiry.dto.InquirySummaryResponse;
import com.eneifour.fantry.inquiry.service.InquiryService;
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
 * 사용자용 1:1 문의 관련 API를 제공하는 컨트롤러입니다.
 * <p>모든 API는 인증된 사용자만 접근할 수 있습니다.
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
     * 새로운 1:1 문의를 등록합니다.
     * <p>요청 본문(Request Body)에 문의 내용을 담아 전송해야 합니다.
     *
     * @param request     문의 생성에 필요한 데이터 (제목, 내용, 카테고리 ID).
     *                    <p><b>[카테고리(csTypeId) ID]</b></p>
     *                    <ul>
     *                      <li>1: 배송문의</li>
     *                      <li>2: 결제문의</li>
     *                      <li>3: 기타문의</li>
     *                      <li>4: 상품문의</li>
     *                      <li>5: 환불/반품 문의</li>
     *                      <li>6: 판매 문의</li>
     *                    </ul>
     * @return 생성된 문의의 요약 정보.
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
     * 특정 1:1 문의에 파일을 첨부합니다.
     *
     * @param inquiryId   파일을 첨부할 문의의 ID
     * @param files       첨부할 파일 목록 (MultipartFile 리스트)
     * @return 작업 성공 시 200 OK.
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
     * 현재 로그인한 사용자가 작성한 1:1 문의 목록을 페이징하여 조회합니다.
     *
     * @param pageable    페이징 정보 (페이지 번호, 페이지 크기, 정렬).
     * @return 페이징 처리된 나의 문의 요약 목록.
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
     * 나의 특정 1:1 문의 상세 내용을 조회합니다.
     *
     * @param inquiryId   조회할 문의의 ID
     * @return 문의 상세 정보.
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
