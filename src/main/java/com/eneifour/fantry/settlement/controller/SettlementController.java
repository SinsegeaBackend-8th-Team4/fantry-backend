package com.eneifour.fantry.settlement.controller;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.security.dto.CustomUserDetails;
import com.eneifour.fantry.settlement.dto.SettlementDetailResponse;
import com.eneifour.fantry.settlement.dto.SettlementSummaryResponse;
import com.eneifour.fantry.settlement.service.SettlementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 판매자 본인의 정산 내역 관련 API를 제공하는 컨트롤러.
 */
@RestController
@RequestMapping("/api/my/settlements")
@RequiredArgsConstructor
@Tag(name = "My Settlement", description = "판매자 정산 내역 조회 API")
public class SettlementController {

    private final SettlementService settlementService;

    /**
     * 현재 로그인한 판매자의 정산 내역 목록을 조회합니다.
     * @param userDetails 현재 로그인한 판매자 정보
     * @param pageable    페이징 정보
     * @return 페이징된 정산 내역 목록
     */
    @GetMapping
    @Operation(summary = "Get My Settlement List", description = "나의 정산 내역 목록을 페이징하여 조회합니다.")
    public ResponseEntity<Page<SettlementSummaryResponse>> getMySettlements(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Pageable pageable
    ) {
        Member seller = userDetails.getMember();
        Page<SettlementSummaryResponse> response = settlementService.getMySettlements(seller, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * 현재 로그인한 판매자의 특정 정산 건 상세 내역을 조회합니다.
     * @param userDetails  현재 로그인한 판매자 정보
     * @param settlementId 정산 ID
     * @return 정산 상세 정보
     */
    @GetMapping("/{settlementId}")
    @Operation(summary = "Get My Settlement Detail", description = "나의 특정 정산 건의 상세 내역을 조회합니다.")
    public ResponseEntity<SettlementDetailResponse> getMySettlementDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable int settlementId
    ) {
        Member seller = userDetails.getMember();
        SettlementDetailResponse response = settlementService.getMySettlementDetail(seller, settlementId);
        return ResponseEntity.ok(response);
    }
}
