package com.eneifour.fantry.settlement.controller;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.security.dto.CustomUserDetails;
import com.eneifour.fantry.settlement.dto.SettlementSettingRequest;
import com.eneifour.fantry.settlement.dto.SettlementSettingResponse;
import com.eneifour.fantry.settlement.service.SettlementAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
