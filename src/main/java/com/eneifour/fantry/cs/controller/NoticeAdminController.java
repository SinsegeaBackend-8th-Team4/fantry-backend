package com.eneifour.fantry.cs.controller;

import com.eneifour.fantry.cs.dto.NoticeCreateRequest;
import com.eneifour.fantry.cs.dto.NoticeDetailResponse;
import com.eneifour.fantry.cs.dto.NoticeUpdateRequest;
import com.eneifour.fantry.cs.service.NoticeAdminService;
import com.eneifour.fantry.cs.service.NoticeService;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.security.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * 관리자용 공지사항 API 엔드포인트입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/cs/notices")
public class NoticeAdminController {

    private final NoticeAdminService noticeAdminService;
    private final NoticeService noticeService; // 상세 조회는 사용자용 서비스 재사용

    /**
     * 새로운 공지사항을 생성합니다.
     */
    @PostMapping
    public ResponseEntity<NoticeDetailResponse> createNotice(
            @RequestBody @Valid NoticeCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member admin = userDetails.getMember();
        NoticeDetailResponse response = noticeAdminService.createNotice(request, admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 기존 공지사항을 수정합니다.
     */
    @PatchMapping("/{noticeId}")
    public ResponseEntity<NoticeDetailResponse> updateNotice(
            @PathVariable int noticeId,
            @RequestBody @Valid NoticeUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member admin = userDetails.getMember();
        NoticeDetailResponse response = noticeAdminService.updateNotice(noticeId, request, admin);
        return ResponseEntity.ok(response);
    }

    /**
     * 공지사항을 삭제합니다.
     */
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(
            @PathVariable int noticeId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Member admin = userDetails.getMember();
        noticeAdminService.deleteNotice(noticeId, admin);
        return ResponseEntity.noContent().build();
    }

    /**
     * 공지사항 상세 정보를 조회합니다. (관리자용)
     */
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDetailResponse> getNoticeDetail(@PathVariable int noticeId) {
        NoticeDetailResponse notice = noticeService.getNotice(noticeId);
        return ResponseEntity.ok(notice);
    }

    /**
     * 특정 공지사항에 파일을 첨부합니다.
     */
    @PostMapping(value = "/{noticeId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addAttachments(
            @PathVariable int noticeId,
            @RequestParam("files") List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member admin = userDetails.getMember();
        noticeAdminService.addAttachments(noticeId, files, admin);
        return ResponseEntity.ok().build();
    }
}
