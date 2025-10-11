package com.eneifour.fantry.inspection.controller;

import com.eneifour.fantry.inspection.domain.InspectionStatus;
import com.eneifour.fantry.inspection.dto.InspectionListResponse;
import com.eneifour.fantry.inspection.dto.InspectionRejectRequest;
import com.eneifour.fantry.inspection.dto.OfflineInspectionDetailResponse;
import com.eneifour.fantry.inspection.dto.OnlineInspectionDetailResponse;
import com.eneifour.fantry.inspection.service.InspectionService;
import com.eneifour.fantry.inspection.support.api.InspectionApiResponse;
import com.eneifour.fantry.inspection.support.api.InspectionPageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        log.debug("statuses={}", statuses);
        InspectionPageResponse<InspectionListResponse> data = inspectionService.getInspectionsByStatuses(statuses, pageable);
        log.debug("data={}", data);
        return InspectionApiResponse.ok(data);
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
    public InspectionApiResponse<Void> approveFirstInspection (@PathVariable int productInspectionId) {
        inspectionService.approveFirstInspection(productInspectionId);
        return InspectionApiResponse.ok(null);
    }
    
    /**
     * 1차 검수 반려
     */
    @PostMapping("/{productInspectionId}/firstReject")
    public InspectionApiResponse<Void> rejectFirstInspection (
            @PathVariable int productInspectionId, @RequestBody @Valid InspectionRejectRequest request
    ) {
        inspectionService.rejectFirstInspection(productInspectionId, request);
        return InspectionApiResponse.ok(null);
    }
}

