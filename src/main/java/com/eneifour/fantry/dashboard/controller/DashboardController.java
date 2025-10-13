package com.eneifour.fantry.dashboard.controller;

import com.eneifour.fantry.dashboard.dto.DashboardSummaryResponse;
import com.eneifour.fantry.dashboard.service.DashboardService;
import com.eneifour.fantry.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DashboardController (신규)
 * 관리자 대시보드 관련 API를 제공하는 컨트롤러.
 */
@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@Tag(name = "Admin Dashboard Management", description = "관리자 대시보드 관련 API")
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * 관리자 대시보드에 필요한 모든 요약 정보를 조회한다.
     * @param userDetails 현재 로그인한 관리자 정보 (인증 확인용)
     * @return DashboardSummaryResponse DTO
     */
    @GetMapping("/summary")
    @Operation(summary = "Get Dashboard Summary", description = "대시보드 요약 정보를 조회합니다.")
    public ResponseEntity<DashboardSummaryResponse> getDashboardSummary(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        DashboardSummaryResponse summary = dashboardService.getDashboardSummary();
        return ResponseEntity.ok(summary);
    }
}
