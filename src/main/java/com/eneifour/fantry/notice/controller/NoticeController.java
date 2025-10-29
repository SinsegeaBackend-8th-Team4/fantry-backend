package com.eneifour.fantry.notice.controller;

import com.eneifour.fantry.notice.dto.NoticeDetailResponse;
import com.eneifour.fantry.notice.dto.NoticeSearchRequest;
import com.eneifour.fantry.notice.dto.NoticeSummaryResponse;
import com.eneifour.fantry.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "공지사항 API (사용자)", description = "사용자용 공지사항 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cs/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 목록 조회 (검색)", description = "공개된(ACTIVE, PINNED) 공지사항 목록을 검색 조건에 따라 페이징하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<NoticeSummaryResponse>> searchNotices(
            @Parameter(description = "검색 조건 (유형 ID, 키워드). 'status' 필드는 무시됩니다.") @ModelAttribute NoticeSearchRequest request,
            @Parameter(description = "페이징 정보 (page, size, sort)") Pageable pageable
    ) {
        Page<NoticeSummaryResponse> results = noticeService.searchNoticesForUser(request, pageable);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "공지사항 상세 조회", description = "특정 공지사항의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = NoticeDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 공지사항")
    })
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDetailResponse> getNoticeDetail(
            @Parameter(description = "조회할 공지사항의 ID", required = true) @PathVariable int noticeId
    ) {
        NoticeDetailResponse notice = noticeService.getNotice(noticeId);
        return ResponseEntity.ok(notice);
    }
}