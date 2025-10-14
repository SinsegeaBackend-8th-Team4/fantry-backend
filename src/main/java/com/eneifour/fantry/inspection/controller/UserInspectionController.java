package com.eneifour.fantry.inspection.controller;

import com.eneifour.fantry.inspection.dto.InspectionRequest;
import com.eneifour.fantry.inspection.dto.MyInspectionResponse;
import com.eneifour.fantry.inspection.service.InspectionService;
import com.eneifour.fantry.inspection.support.api.InspectionApiResponse;
import com.eneifour.fantry.inspection.support.api.InspectionPageResponse;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 유저용 검수 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/inspections")
@RequiredArgsConstructor
public class UserInspectionController {
    private final InspectionService inspectionService;

    /**
     * 1차 검수 신청 생성
     */
    @PostMapping
    public InspectionApiResponse<Integer> createInspection(
            @RequestPart("request") InspectionRequest request,
            @RequestPart("files") List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Member member = userDetails.getMember();
        int inspectionID = inspectionService.createInspection(member.getMemberId(), request, files);

        return InspectionApiResponse.ok(inspectionID);
    }

    /**
     * 나의 검수 현황 목록 조회
     */
    @GetMapping("/my")
    public InspectionApiResponse<InspectionPageResponse<MyInspectionResponse>> getMyInspections(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size=20, sort="submittedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Member member = userDetails.getMember();
        InspectionPageResponse<MyInspectionResponse> data = inspectionService.getMyInspections(member.getMemberId(), pageable);

        return InspectionApiResponse.ok(data);
    }

    /**
     * 1차 승인된 상품의 발송을 확인하고, 2차 검수 상태 변경
     * @param productInspectionId 상태를 변경할 검수 ID
     * @param userDetails 현재 로그인한 사용자 정보
     * @return 성공 응답
     */
    @PostMapping("/{productInspectionId}/start-offline")
    public InspectionApiResponse<Void> startOffline(@PathVariable int productInspectionId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member member = userDetails.getMember();
        inspectionService.startOfflineInspection(productInspectionId, member.getMemberId());

        return InspectionApiResponse.ok(null);
    }
}
