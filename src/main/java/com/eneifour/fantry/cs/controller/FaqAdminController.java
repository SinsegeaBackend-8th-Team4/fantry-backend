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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/cs/faq")
public class FaqAdminController {

    // 관리자 기능은 FaqAdminService가 책임져야 한다.
    private final FaqAdminService faqAdminService;
    // 관리자도 사용자처럼 '검색' 기능은 필요하므로, FaqService도 함께 주입받는다.
    private final FaqService faqService;

    @PostMapping
    public ResponseEntity<FaqResponse> createFaq(
            @RequestBody @Valid FaqCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member admin = userDetails.getMember();
        // [수정!] 올바른 서비스를 호출한다.
        FaqResponse response = faqAdminService.createFaq(request, admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/{faqId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addAttachments(
            @PathVariable int faqId,
            @RequestParam("files") List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member admin = userDetails.getMember();
        faqAdminService.addAttachments(faqId, files, admin); // [수정!]
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{faqId}")
    public ResponseEntity<FaqResponse> updateFaq(
            @PathVariable int faqId,
            @RequestBody @Valid FaqUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member admin = userDetails.getMember();
        FaqResponse response = faqAdminService.updateFaq(faqId, request, admin); // [수정!]
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{faqId}")
    public ResponseEntity<Void> deleteFaq(
            @PathVariable int faqId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Member admin = userDetails.getMember();
        faqAdminService.deleteFaq(faqId, admin); // [수정!]
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<FaqResponse>> searchFaqs(
            @ModelAttribute FaqSearchRequest request,
            Pageable pageable
    ) {
        // 검색 기능은 사용자용 서비스를 재사용한다.
        Page<FaqResponse> results = faqService.searchFaqs(request, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{faqId}")
    public ResponseEntity<FaqResponse> getFaqDetail(@PathVariable int faqId) {
        FaqResponse faq = faqAdminService.getFaq(faqId); // [수정!]
        return ResponseEntity.ok(faq);
    }
}