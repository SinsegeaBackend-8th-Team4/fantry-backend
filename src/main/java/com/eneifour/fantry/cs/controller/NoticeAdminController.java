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
 * 관리자용 공지사항 관련 API를 제공하는 컨트롤러입니다.
 * <p>모든 API는 관리자 권한을 가진 사용자만 접근할 수 있습니다.
 *
 * @author 정재환
 * @since 2025.10.11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/cs/notices")
public class NoticeAdminController {

    private final NoticeAdminService noticeAdminService;
    private final NoticeService noticeService; // 상세 조회는 사용자용 서비스 재사용

    /**
     * 새로운 공지사항을 등록합니다.
     *
     * @param request     공지사항 생성에 필요한 데이터 (제목, 내용, 카테고리 ID). 
     *                    <p><b>[카테고리(csTypeId) ID]</b></p>
     *                    <ul>
     *                      <li>1: 배송문의</li>
     *                      <li>2: 결제문의</li>
     *                      <li>3: 기타문의</li>
     *                      <li>4: 상품문의</li>
     *                      <li>5: 환불/반품 문의</li>
     *                      <li>6: 판매 문의</li>
     *                    </ul>
     * @return 생성된 공지사항의 상세 정보.
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
     * 특정 공지사항의 내용을 수정합니다.
     *
     * @param noticeId    수정할 공지사항의 ID
     * @param request     공지사항 수정에 필요한 데이터.
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
     * @return 수정된 공지사항의 상세 정보.
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
     * 특정 공지사항을 삭제합니다.
     *
     * @param noticeId    삭제할 공지사항의 ID
     * @return 작업 성공 시 204 No Content.
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
     * 특정 공지사항의 상세 정보를 조회합니다.
     * <p>이 API는 사용자용 API와 동일한 기능을 수행합니다.
     *
     * @param noticeId 조회할 공지사항의 ID.
     * @return 공지사항 상세 정보.
     */
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDetailResponse> getNoticeDetail(@PathVariable int noticeId) {
        NoticeDetailResponse notice = noticeService.getNotice(noticeId);
        return ResponseEntity.ok(notice);
    }

    /**
     * 특정 공지사항에 파일을 첨부합니다.
     *
     * @param noticeId    파일을 첨부할 공지사항의 ID
     * @param files       첨부할 파일 목록
     * @return 작업 성공 시 200 OK.
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
