package com.eneifour.fantry.settlement.controller;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.security.dto.CustomUserDetails;
import com.eneifour.fantry.settlement.dto.SettlementSettingRequest;
import com.eneifour.fantry.settlement.dto.SettlementSettingResponse;
import com.eneifour.fantry.settlement.service.SettlementAdminService;
import com.eneifour.fantry.settlement.dto.SettlementDashboardResponse;
import com.eneifour.fantry.settlement.dto.SettlementDetailResponse;
import com.eneifour.fantry.settlement.dto.SettlementSearchCondition;
import com.eneifour.fantry.settlement.dto.SettlementSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 관리자용 정산 관련 API를 제공하는 컨트롤러.
 */
@RestController
@RequestMapping("/api/admin/settlement")
@RequiredArgsConstructor
@Tag(name = "Admin Settlement Management", description = "관리자 정산 관련 API")
public class SettlementAdminController {

    private final SettlementAdminService settlementAdminService;

    /**
     * 대시보드에 표시될 정산 KPI 요약 정보를 조회합니다.
     * @return 정산 KPI 데이터
     */
    @GetMapping("/dashboard")
    @Operation(summary = "Get Settlement Dashboard Summary", description = "대시보드용 정산 KPI 데이터를 조회합니다.")
    public ResponseEntity<SettlementDashboardResponse> getDashboardSummary() {
        SettlementDashboardResponse response = settlementAdminService.getDashboardSummary();
        return ResponseEntity.ok(response);
    }

    /**
     * 정산 내역 목록을 검색 조건과 함께 페이징하여 조회합니다.
     * @param condition 검색 조건 (판매자명, 상태, 기간 등)
     * @param pageable 페이징 정보
     * @return 페이징된 정산 내역 목록
     */
    @GetMapping
    @Operation(summary = "Get Settlement List", description = "정산 내역 목록을 검색하고 페이징하여 조회합니다.")
    public ResponseEntity<Page<SettlementSummaryResponse>> getSettlements(
            @ModelAttribute SettlementSearchCondition condition,
            Pageable pageable
    ) {
        Page<SettlementSummaryResponse> response = settlementAdminService.getSettlements(condition, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * 특정 정산 건의 상세 내역을 조회합니다.
     * @param settlementId 정산 ID
     * @return 정산 상세 정보
     */
    @GetMapping("/{settlementId}")
    @Operation(summary = "Get Settlement Detail", description = "특정 정산 건의 상세 내역을 조회합니다.")
    public ResponseEntity<SettlementDetailResponse> getSettlementDetail(
            @PathVariable int settlementId
    ) {
        SettlementDetailResponse response = settlementAdminService.getSettlementDetail(settlementId);
        return ResponseEntity.ok(response);
    }

    /**
     * 현재 적용 중인 정산 설정을 조회합니다.
     * @return 정산 설정 정보
     */
    @GetMapping("/settings")
    @Operation(summary = "Get Settlement Settings", description = "현재 정산 설정을 조회합니다.")
    public ResponseEntity<SettlementSettingResponse> getSettlementSettings() {
        SettlementSettingResponse response = settlementAdminService.getSettlementSetting();
        return ResponseEntity.ok(response);
    }

    /**
     * 정산 설정을 생성하거나 수정합니다.
     * @param request     정산 설정 요청 데이터
     * @param userDetails 현재 로그인한 관리자 정보
     * @return 생성 또는 수정된 정산 설정 정보
     */
    @PostMapping("/settings")
    @Operation(summary = "Create or Update Settlement Settings", description = "정산 설정을 생성하거나 수정합니다.")
    public ResponseEntity<SettlementSettingResponse> createOrUpdateSettlementSettings(
            @RequestBody @Valid SettlementSettingRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member admin = userDetails.getMember();
        SettlementSettingResponse response = settlementAdminService.createOrUpdateSettlementSetting(request, admin);
        return ResponseEntity.ok(response);
    }
}
