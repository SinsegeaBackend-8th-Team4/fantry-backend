package com.eneifour.fantry.notice.controller;

import com.eneifour.fantry.notice.dto.NoticeDetailResponse;
import com.eneifour.fantry.notice.dto.NoticeSearchRequest;
import com.eneifour.fantry.notice.dto.NoticeSummaryResponse;
import com.eneifour.fantry.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cs/notices")
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * (사용자용) 공지사항 목록을 검색 조건에 따라 페이징하여 조회합니다.
     * 공개된(ACTIVE, PINNED) 상태의 공지사항만 조회됩니다.
     */
    @GetMapping
    public ResponseEntity<Page<NoticeSummaryResponse>> searchNotices(
            @ModelAttribute NoticeSearchRequest request,
            Pageable pageable
    ) {
        Page<NoticeSummaryResponse> results = noticeService.searchNoticesForUser(request, pageable);
        return ResponseEntity.ok(results);
    }

    /**
     * 특정 공지사항의 상세 정보를 조회합니다.
     */
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDetailResponse> getNoticeDetail(@PathVariable int noticeId) {
        NoticeDetailResponse notice = noticeService.getNotice(noticeId);
        return ResponseEntity.ok(notice);
    }
}