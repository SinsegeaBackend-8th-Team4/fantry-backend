package com.eneifour.fantry.inspection.controller;

import com.eneifour.fantry.inspection.domain.InspectionStatus;
import com.eneifour.fantry.inspection.dto.InspectionListResponse;
import com.eneifour.fantry.inspection.service.InspectionService;
import com.eneifour.fantry.inspection.support.api.InspectionApiResponse;
import com.eneifour.fantry.inspection.support.api.InspectionPageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * - status: SUBMITTED / FIRST_REVIEWED / COMPLETED / REJECTED
     * - page/size/sort는 스프링 Pageable로 자동 파싱 (예: ?page=0&size=20&sort=submittedAt,desc)
     */
    @GetMapping
    public InspectionApiResponse<InspectionPageResponse<InspectionListResponse>> listByStatus(
            @RequestParam InspectionStatus status,
            @PageableDefault(size=20, sort="submittedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        log.debug("status={}", status);
        log.debug("pageable={}", pageable);

        InspectionPageResponse<InspectionListResponse> data = inspectionService.getInspectionsByStatus(status, pageable);
        log.debug("data={}", data);
        return InspectionApiResponse.ok(data);
    }

    /**
     * [POST] 1차 검수 승인/반려 (온라인 → 오프라인/반려)
     * - 요청 바디: { decision: APPROVE | REJECT, reason?: string }
     * - APPROVE → status: FIRST_REVIEWED
     * - REJECT  → status: REJECTED (reason 저장)
     */
    // @PostMapping("/{inspectionId}/first-review")
    // public InspectionApiResponse<Void> firstReview(@PathVariable int inspectionId,
    //    @RequestBody FirstReviewRequest req) { ... }

    /**
     * 상세 조회
     * - FE 상세 화면 필요 시 사용
     */
    // @GetMapping("/{inspectionId}")
    // public InspectionApiResponse<InspectionDetailDto> getDetail(@PathVariable int inspectionId) { ... }
}

