package com.eneifour.fantry.cs.controller;

import com.eneifour.fantry.cs.dto.NoticeDetailResponse;
import com.eneifour.fantry.cs.dto.NoticeSearchRequest;
import com.eneifour.fantry.cs.dto.NoticeSummaryResponse;
import com.eneifour.fantry.cs.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자용 공지사항 API 엔드포인트입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cs/notices")
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 공지사항 목록을 조건에 따라 조회합니다. (페이징 포함)
     */
    @GetMapping
    public ResponseEntity<Page<NoticeSummaryResponse>> searchNotices(
            @ModelAttribute NoticeSearchRequest request,
            Pageable pageable
    ) {
        Page<NoticeSummaryResponse> results = noticeService.searchNotices(request, pageable);
        return ResponseEntity.ok(results);
    }

    /**
     * 공지사항 상세 정보를 조회합니다.
     */
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDetailResponse> getNoticeDetail(@PathVariable int noticeId) {
        NoticeDetailResponse notice = noticeService.getNotice(noticeId);
        return ResponseEntity.ok(notice);
    }
}
