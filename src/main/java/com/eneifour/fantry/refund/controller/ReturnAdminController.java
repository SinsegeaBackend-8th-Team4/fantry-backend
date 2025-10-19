package com.eneifour.fantry.refund.controller;

import com.eneifour.fantry.refund.dto.*;
import com.eneifour.fantry.refund.service.ReturnAdminService;
import com.eneifour.fantry.security.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 관리자용 환불/반품 관련 API를 제공하는 컨트롤러입니다.
 * <p>모든 API는 관리자 권한을 가진 사용자만 접근할 수 있습니다.
 *
 * @author 정재환
 * @since 2025.10.12
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/returns")
public class ReturnAdminController {

    private final ReturnAdminService returnAdminService;

    /**
     * 관리자용 대시보드에 표시될 환불/반품 요청 통계를 조회합니다.
     *
     * @return 각 상태별 요청 건수를 포함하는 통계 데이터.
     */
    @GetMapping("/stats")
    public ResponseEntity<ReturnStatsAdminResponse> getReturnStats() {
        ReturnStatsAdminResponse stats = returnAdminService.getReturnStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * 관리자가 사용자를 대신하여 환불/반품 요청을 생성합니다.
     * <p>고객센터를 통해 접수된 건 등을 관리자가 직접 시스템에 등록할 때 사용됩니다.
     *
     * @param request 환불/반품 요청 생성에 필요한 데이터 (주문 ID, 회원 ID, 사유 등).
     *                <p><b>[환불/반품 사유(reason)]</b></p>
     *                <ul>
     *                    <li>SIMPLE_CHANGE_OF_MIND: 단순 변심</li>
     *                    <li>PRODUCT_DEFECT: 상품 하자</li>
     *                    <li>SHIPPING_ERROR: 배송 오류</li>
     *                    <li>ETC: 기타</li>
     *                </ul>
     * @return 생성된 환불/반품 요청의 상세 정보.
     */
    @PostMapping
    public ResponseEntity<ReturnAdminResponse> createReturnRequest(
            @RequestBody @Valid ReturnAdminCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ReturnAdminResponse response = returnAdminService.createReturnRequestByAdmin(request, userDetails.getMember());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 환불/반품 요청 목록을 검색 조건에 따라 페이징하여 조회합니다.
     *
     * @param request  검색 조건 DTO (처리 상태, 구매자 이름).
     *                 <p><b>[검색 가능한 처리 상태(status)]</b></p>
     *                 <ul>
     *                     <li>REQUESTED: 요청됨</li>
     *                     <li>IN_TRANSIT: 수거 중</li>
     *                     <li>INSPECTING: 검수 중</li>
     *                     <li>APPROVED: 승인</li>
     *                     <li>REJECTED: 거절</li>
     *                     <li>COMPLETED: 처리 완료</li>
     *                     <li>USER_CANCELLED: 사용자 철회</li>
     *                     <li>DELETED: 삭제됨</li>
     *                 </ul>
     * @param pageable 페이징 정보 (페이지 번호, 페이지 크기, 정렬).
     * @return 페이징 처리된 환불/반품 요청 요약 목록.
     */
    @GetMapping
    public ResponseEntity<Page<ReturnSummaryResponse>> searchReturnRequests(
            @ModelAttribute ReturnSearchRequest request,
            Pageable pageable
    ) {
        Page<ReturnSummaryResponse> results = returnAdminService.searchReturnRequests(request, pageable);
        return ResponseEntity.ok(results);
    }

    /**
     * 특정 환불/반품 요청의 상세 정보를 조회합니다.
     *
     * @param returnRequestId 조회할 환불/반품 요청의 ID
     * @return 관리자용 환불/반품 요청 상세 정보.
     */
    @GetMapping("/{returnRequestId}")
    public ResponseEntity<ReturnAdminResponse> getReturnRequestDetail(
            @PathVariable int returnRequestId
    ) {
        ReturnAdminResponse response = returnAdminService.getReturnRequestDetail(returnRequestId);
        return ResponseEntity.ok(response);
    }

    /**
     * 특정 환불/반품 요청의 상태를 변경하고 관련 정보를 업데이트합니다.
     * <p>환불 승인, 거절, 검수 등의 처리를 이 API를 통해 수행합니다.
     *
     * @param returnRequestId 처리할 환불/반품 요청의 ID
     * @param request         업데이트할 상태 및 관련 데이터.
     *                        <p><b>[변경 가능한 처리 상태(status)]</b></p>
     *                        <ul>
     *                            <li>REQUESTED: 요청됨</li>
     *                            <li>IN_TRANSIT: 수거 중</li>
     *                            <li>INSPECTING: 검수 중</li>
     *                            <li>APPROVED: 승인</li>
     *                            <li>REJECTED: 거절</li>
     *                            <li>COMPLETED: 처리 완료</li>
     *                        </ul>
     *                        <p>상태를 'REJECTED'로 변경 시 'rejectReason' 필드가 필요할 수 있습니다.</p>
     * @return 상태가 변경된 환불/반품 요청의 상세 정보.
     */
    @PatchMapping("/{returnRequestId}")
    public ResponseEntity<ReturnAdminResponse> updateReturnRequestStatus(
            @PathVariable int returnRequestId,
            @RequestBody @Valid ReturnAdminUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ReturnAdminResponse response = returnAdminService.updateReturnRequestStatus(returnRequestId, request, userDetails.getMember());
        return ResponseEntity.ok(response);
    }

    /**
     * 관리자가 환불/반품 요청을 논리적으로 삭제합니다.
     * <p>시스템에서 해당 요청을 숨김 처리하며, 물리적으로 데이터가 삭제되지는 않습니다.
     *
     * @param returnRequestId 삭제할 환불/반품 요청의 ID
     * @return 작업 성공 시 204 No Content.
     */
    @DeleteMapping("/{returnRequestId}")
    public ResponseEntity<Void> deleteReturnRequest(
            @PathVariable int returnRequestId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        returnAdminService.deleteReturnRequest(returnRequestId, userDetails.getMember());
        return ResponseEntity.noContent().build();
    }
}
