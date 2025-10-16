package com.eneifour.fantry.notice.controller;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.notice.dto.*;
import com.eneifour.fantry.notice.service.NoticeAdminService;
import com.eneifour.fantry.notice.service.NoticeService;
import com.eneifour.fantry.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.eneifour.fantry.notice.dto.NoticeStatsAdminResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "공지사항 API (관리자)", description = "관리자용 공지사항 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/cs/notices")
public class NoticeAdminController {

    private final NoticeAdminService noticeAdminService;
    private final NoticeService noticeService; // 상세 조회는 사용자용 서비스 재사용

    @Operation(summary = "공지사항 목록 조회 (관리자)", description = "모든 공지사항 목록을 검색 조건에 따라 페이징하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<NoticeSummaryResponse>> searchNotices(
            @Parameter(description = "검색 조건 (키워드, 유형, 상태)") @ModelAttribute NoticeSearchRequest request,
            @Parameter(description = "페이징 정보 (page, size, sort)") Pageable pageable
    ) {
        Page<NoticeSummaryResponse> results = noticeAdminService.searchNoticesForAdmin(request, pageable);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "신규 공지사항 등록", description = "새로운 공지사항을 등록합니다. 'status' 필드를 통해 상태(DRAFT, ACTIVE, PINNED, INACTIVE)를 지정할 수 있으며, 생략 시 기본값은 DRAFT 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "등록 성공",
                    content = @Content(schema = @Schema(implementation = NoticeDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping
    public ResponseEntity<NoticeDetailResponse> createNotice(
            @RequestBody @Valid NoticeCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member admin = userDetails.getMember();
        NoticeDetailResponse response = noticeAdminService.createNotice(request, admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "공지사항 수정", description = "특정 공지사항의 내용을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = NoticeDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 공지사항")
    })
    @PatchMapping("/{noticeId}")
    public ResponseEntity<NoticeDetailResponse> updateNotice(
            @Parameter(description = "수정할 공지사항의 ID", required = true) @PathVariable int noticeId,
            @RequestBody @Valid NoticeUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member admin = userDetails.getMember();
        NoticeDetailResponse response = noticeAdminService.updateNotice(noticeId, request, admin);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "공지사항 삭제", description = "특정 공지사항을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 공지사항")
    })
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(
            @Parameter(description = "삭제할 공지사항의 ID", required = true) @PathVariable int noticeId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Member admin = userDetails.getMember();
        noticeAdminService.deleteNotice(noticeId, admin);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "공지사항 상세 조회 (관리자)", description = "특정 공지사항의 상세 정보를 조회합니다. 사용자용 API와 동일한 기능을 수행합니다.")
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

    @Operation(summary = "공지사항 파일 첨부", description = "특정 공지사항에 파일을 첨부합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "파일 첨부 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 공지사항")
    })
    @PostMapping(value = "/{noticeId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addAttachments(
            @Parameter(description = "파일을 첨부할 공지사항의 ID", required = true) @PathVariable int noticeId,
            @Parameter(description = "첨부할 파일 목록", required = true) @RequestParam("files") List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member admin = userDetails.getMember();
        noticeAdminService.addAttachments(noticeId, files, admin);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "공지사항 통계 조회 (관리자)", description = "관리자 대시보드에 필요한 공지사항 통계 정보를 조회합니다. (상태별 개수)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "통계 조회 성공", content = @Content(schema = @Schema(implementation = NoticeStatsAdminResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 또는 권한 없음")
    })
    @GetMapping("/stats")
    public ResponseEntity<NoticeStatsAdminResponse> getNoticeStats() {
        NoticeStatsAdminResponse stats = noticeAdminService.getNoticeStatsForAdmin();
        return ResponseEntity.ok(stats);
    }
}
