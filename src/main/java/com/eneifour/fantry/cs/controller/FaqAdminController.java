package com.eneifour.fantry.cs.controller;

import com.eneifour.fantry.cs.dto.FaqCreateRequest;
import com.eneifour.fantry.cs.dto.FaqResponse;
import com.eneifour.fantry.cs.dto.FaqSearchRequest;
import com.eneifour.fantry.cs.dto.FaqUpdateRequest;
import com.eneifour.fantry.cs.service.FaqAdminService;
import com.eneifour.fantry.cs.service.FaqService;
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
 * 관리자용 FAQ(자주 묻는 질문) 관련 API를 제공하는 컨트롤러입니다.
 * <p>모든 API는 관리자 권한을 가진 사용자만 접근할 수 있습니다.
 *
 * @author 정재환
 * @since 2025.10.11
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/cs/faq")
public class FaqAdminController {

    private final FaqAdminService faqAdminService;
    private final FaqService faqService;

    /**
     * 새로운 FAQ를 등록합니다.
     *
     * @param request     FAQ 생성에 필요한 데이터 (제목, 내용, 카테고리 ID 등).
     *                    <p><b>[카테고리(csTypeId) ID]</b></p>
     *                    <ul>
     *                      <li>1: 배송문의</li>
     *                      <li>2: 결제문의</li>
     *                      <li>3: 기타문의</li>
     *                      <li>4: 상품문의</li>
     *                      <li>5: 환불/반품 문의</li>
     *                      <li>6: 판매 문의</li>
     *                    </ul>
     * @return 생성된 FAQ 정보.
     */
    @PostMapping
    public ResponseEntity<FaqResponse> createFaq(
            @RequestBody @Valid FaqCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member admin = userDetails.getMember();
        FaqResponse response = faqAdminService.createFaq(request, admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 특정 FAQ에 파일을 첨부합니다.
     *
     * @param faqId       파일을 첨부할 FAQ의 ID
     * @param files       첨부할 파일 목록
     * @return 작업 성공 시 200 OK.
     */
    @PostMapping(value = "/{faqId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addAttachments(
            @PathVariable int faqId,
            @RequestParam("files") List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member admin = userDetails.getMember();
        faqAdminService.addAttachments(faqId, files, admin);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 FAQ의 내용을 수정합니다.
     *
     * @param faqId       수정할 FAQ의 ID
     * @param request     FAQ 수정에 필요한 데이터.
     *                    <p><b>[변경 가능한 상태(status)]</b></p>
     *                    <ul>
     *                       <li>DRAFT: 임시저장</li>
     *                       <li>ACTIVE: 활성 (노출)</li>
     *                       <li>PINNED: 상단 고정</li>
     *                       <li>INACTIVE: 비활성 (미노출)</li>
     *                    </ul>
     *                    <p><b>[카테고리(csTypeId) ID]</b></p>
     *                    <ul>
     *                      <li>1: 배송문의</li>
     *                      <li>2: 결제문의</li>
     *                      <li>3: 기타문의</li>
     *                      <li>4: 상품문의</li>
     *                      <li>5: 환불/반품 문의</li>
     *                      <li>6: 판매 문의</li>
     *                    </ul>
     * @return 수정된 FAQ 정보.
     */
    @PatchMapping("/{faqId}")
    public ResponseEntity<FaqResponse> updateFaq(
            @PathVariable int faqId,
            @RequestBody @Valid FaqUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member admin = userDetails.getMember();
        FaqResponse response = faqAdminService.updateFaq(faqId, request, admin);
        return ResponseEntity.ok(response);
    }

    /**
     * 특정 FAQ를 삭제합니다.
     *
     * @param faqId       삭제할 FAQ의 ID
     * @return 작업 성공 시 204 No Content.
     */
    @DeleteMapping("/{faqId}")
    public ResponseEntity<Void> deleteFaq(
            @PathVariable int faqId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Member admin = userDetails.getMember();
        faqAdminService.deleteFaq(faqId, admin);
        return ResponseEntity.noContent().build();
    }

    /**
     * FAQ 목록을 검색 조건에 따라 페이징하여 조회합니다.
     *
     * @param request  검색 조건 DTO.
     *                 <p><b>[검색 가능한 상태(status)]</b></p>
     *                  <ul>
     *                      <li>DRAFT: 임시저장</li>
     *                      <li>ACTIVE: 활성 (노출)</li>
     *                      <li>PINNED: 상단 고정</li>
     *                      <li>INACTIVE: 비활성 (미노출)</li>
     *                  </ul>
     *                  <p><b>[카테고리(csTypeId) ID]</b></p>
     *                  <ul>
     *                      <li>1: 배송문의</li>
     *                      <li>2: 결제문의</li>
     *                      <li>3: 기타문의</li>
     *                      <li>4: 상품문의</li>
     *                      <li>5: 환불/반품 문의</li>
     *                      <li>6: 판매 문의</li>
     *                  </ul>
     * @param pageable 페이징 정보 (페이지 번호, 페이지 크기, 정렬).
     * @return 페이징 처리된 FAQ 목록.
     */
    @GetMapping
    public ResponseEntity<Page<FaqResponse>> searchFaqs(
            @ModelAttribute FaqSearchRequest request,
            Pageable pageable
    ) {
        Page<FaqResponse> results = faqService.searchFaqs(request, pageable);
        return ResponseEntity.ok(results);
    }

    /**
     * 특정 FAQ의 상세 정보를 조회합니다.
     *
     * @param faqId 조회할 FAQ의 ID.
     * @return FAQ 상세 정보.
     */
    @GetMapping("/{faqId}")
    public ResponseEntity<FaqResponse> getFaqDetail(@PathVariable int faqId) {
        FaqResponse faq = faqAdminService.getFaq(faqId);
        return ResponseEntity.ok(faq);
    }
}