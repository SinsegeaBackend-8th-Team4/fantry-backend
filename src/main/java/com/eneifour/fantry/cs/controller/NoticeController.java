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
 * 사용자용 공지사항 관련 API를 제공하는 컨트롤러입니다.
 *
 * @author 정재환
 * @since 2025.10.11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cs/notices")
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 공지사항 목록을 검색 조건에 따라 페이징하여 조회합니다.
     * <p>검색 조건(카테고리, 키워드)은 선택 사항이며, 조건이 없으면 'ACTIVE' 또는 'PINNED' 상태의 전체 목록이 조회됩니다.
     *
     * @param request  검색 조건 DTO.
     *                 <p><b>[카테고리(csTypeId) ID]</b></p>
     *                 <ul>
     *                     <li>1: 배송문의</li>
     *                     <li>2: 결제문의</li>
     *                     <li>3: 기타문의</li>
     *                     <li>4: 상품문의</li>
     *                     <li>5: 환불/반품 문의</li>
     *                     <li>6: 판매 문의</li>
     *                 </ul>
     *                 <p>키워드(keyword)는 제목 또는 내용에서 검색됩니다.</p>
     * @param pageable 페이징 정보 (페이지 번호, 페이지 크기, 정렬).
     * @return 페이징 처리된 공지사항 요약 목록.
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
     * 특정 공지사항의 상세 내용을 조회합니다.
     *
     * @param noticeId 조회할 공지사항의 ID.
     * @return 공지사항 상세 정보.
     */
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDetailResponse> getNoticeDetail(@PathVariable int noticeId) {
        NoticeDetailResponse notice = noticeService.getNotice(noticeId);
        return ResponseEntity.ok(notice);
    }
}
