package com.eneifour.fantry.inspection.controller;

import com.eneifour.fantry.inspection.domain.InspectionStatus;
import com.eneifour.fantry.inspection.domain.InventoryStatus;
import com.eneifour.fantry.inspection.dto.*;
import com.eneifour.fantry.inspection.service.InspectionService;
import com.eneifour.fantry.inspection.support.api.InspectionApiResponse;
import com.eneifour.fantry.inspection.support.api.InspectionPageResponse;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.security.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 관리자용 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/inspections")
@RequiredArgsConstructor
public class AdminInspectionController {
    private final InspectionService inspectionService;

    /**
     * 상태별 신청 목록 (페이지)
     * @param statuses 조회할 검수 상태 목록 (e.g. ?statuses=SUBMITTED,FIRST_REVIEWED)
     * @param pageable 페이지네이션, 정렬 정보 (e.g. ?page=0&size=20&sort=submittedAt,desc)
     * @return 페이징 처리된 검수 목록
     */
    @GetMapping
    public InspectionApiResponse<InspectionPageResponse<InspectionListResponse>> listByStatus (
            @RequestParam List<InspectionStatus> statuses,
            @PageableDefault(size=20, sort="submittedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        InspectionPageResponse<InspectionListResponse> data = inspectionService.getInspectionsByStatuses(statuses, pageable);
        return InspectionApiResponse.ok(data);
    }

    /**
     * 재고 상태별 신청 목록 (페이지)
     * @param statuses 조회할 재고 상태 목록 (e.g. ?statuses=PENDING)
     * @param pageable 페이지네이션, 정렬 정보 (e.g. ?page=0&size=20&sort=submittedAt,desc)
     * @return 페이징 처리된 검수 목록
     */
    @GetMapping("/inventories")
    public InspectionApiResponse<InspectionPageResponse<InventoryListResponse>> listByInventoryStatus(
            @RequestParam List<InventoryStatus> statuses,
            @PageableDefault(size=20, sort="submittedAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        InspectionPageResponse<InventoryListResponse> data = inspectionService.getInspectionsByInventoryStatuses(statuses, pageable);
        return InspectionApiResponse.ok(data);
    }

    /**
     * 특정 검수의 재고 상태를 변경합니다.
     * @param productInspectionId 변경할 검수 ID (e.g. /api/admin/inspections/{productInspectionId}/inventory)
     * @param status 새로운 재고 상태 (e.g. ?status=ACTIVE)
     * @return 성공 메시지
     */
    @PutMapping("/{productInspectionId}/inventory/status")
    public InspectionApiResponse<Map<String, Object>> updateInventoryStatus(
            @PathVariable int productInspectionId,
            @RequestParam InventoryStatus status
    ) {
        inspectionService.updateInventoryStatus(productInspectionId, status);
        log.info("검수 ID {}의 재고 상태 변경 요청 수신: {}", productInspectionId, status);

        return InspectionApiResponse.ok(Map.of(
                "productInspectionId", productInspectionId,
                "newStatus", status,
                "message", "재고 상태가 성공적으로 변경되었습니다."
        ));
    }


    /**
     * 관리자 1차 온라인 검수 상세 조회
     * @param productInspectionId 조회할 검수 ID
     * @return 1차 온라인 검수 상세 정보
     */
    @GetMapping("/online/{productInspectionId}")
    public InspectionApiResponse<OnlineInspectionDetailResponse> getOnlineInspectionDetail (@PathVariable int productInspectionId) {
        OnlineInspectionDetailResponse onlineInspectionDetail = inspectionService.getOnlineInspectionDetail(productInspectionId);
        return InspectionApiResponse.ok(onlineInspectionDetail);
    }

    /**
     * 관리자 2차 오프라인 검수 상세 조회
     * @param productInspectionId 조회할 검수 ID
     * @return 2차 오프라인 상세 정보
     */
    @GetMapping("/offline/{productInspectionId}")
    public InspectionApiResponse<OfflineInspectionDetailResponse> getOfflineInspectionDetail (@PathVariable int productInspectionId) {
        OfflineInspectionDetailResponse offlineInspectionDetail = inspectionService.getOfflineInspectionDetail(productInspectionId);
        return InspectionApiResponse.ok(offlineInspectionDetail);
    }

    /**
     * 1차 검수 승인
     */
    @PostMapping("/{productInspectionId}/firstApprove")
    public InspectionApiResponse<Void> approveFirstInspection (
            @PathVariable int productInspectionId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member admin = userDetails.getMember();
        inspectionService.approveFirstInspection(productInspectionId, admin.getMemberId());

        return InspectionApiResponse.ok(null);
    }
    
    /**
     * 1차 검수 반려
     */
    @PostMapping("/{productInspectionId}/firstReject")
    public InspectionApiResponse<Void> rejectFirstInspection (
            @PathVariable int productInspectionId, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody @Valid InspectionRejectRequest request
    ) {
        Member admin = userDetails.getMember();
        inspectionService.rejectFirstInspection(productInspectionId, admin.getMemberId(), request);
        return InspectionApiResponse.ok(null);
    }

    /**
     * 2차 검수 승인
     */
    @PostMapping("/{productInspectionId}/secondApprove")
    public InspectionApiResponse<Void> approveSecondInspection (
            @PathVariable int productInspectionId, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody @Valid OfflineInspectionApproveRequest request
    ) {
        Member admin = userDetails.getMember();
        inspectionService.approveSecondInspection(productInspectionId, admin.getMemberId(), request);
        return InspectionApiResponse.ok(null);
    }

    /**
     * 2차 검수 반려
     */
    @PostMapping("/{productInspectionId}/secondReject")
    public InspectionApiResponse<Void> rejectSecondInspection (
            @PathVariable int productInspectionId, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody @Valid OfflineInspectionRejectRequest request
    ) {
        Member admin = userDetails.getMember();
        inspectionService.rejectSecondInspection(productInspectionId, admin.getMemberId(), request);
        return InspectionApiResponse.ok(null);
    }
}

