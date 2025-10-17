package com.eneifour.fantry.refund.controller;

import com.eneifour.fantry.refund.dto.ReturnCreateRequest;
import com.eneifour.fantry.refund.dto.ReturnDetailResponse;
import com.eneifour.fantry.refund.dto.ReturnSummaryResponse;
import com.eneifour.fantry.refund.service.ReturnService;
import com.eneifour.fantry.security.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * 사용자용 환불/반품 관련 API를 제공하는 컨트롤러입니다.
 * <p>모든 API는 인증된 사용자만 접근할 수 있습니다.
 *
 * @author 정재환
 * @since 2025.10.12
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/returns")
public class ReturnController {

    private final ReturnService returnService;

    /**
     * 새로운 환불/반품을 요청합니다.
     * <p>요청 본문(Request Body)에 주문 ID, 환불 사유 등을 담아 전송해야 합니다.
     *
     * @param request 환불/반품 요청 생성에 필요한 데이터.
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
    public ResponseEntity<ReturnDetailResponse> createReturnRequest(
            @RequestBody @Valid ReturnCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ReturnDetailResponse response = returnService.createReturnRequest(request, userDetails.getMember());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 특정 환불/반품 요청에 증빙 자료(파일)를 첨부합니다.
     *
     * @param returnRequestId 파일을 첨부할 환불/반품 요청의 ID
     * @param files           첨부할 파일 목록 (이미지 등)
     * @return 작업 성공 시 200 OK.
     */
    @PostMapping(value = "/{returnRequestId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addAttachments(
            @PathVariable int returnRequestId,
            @RequestParam("files") List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        returnService.addAttachments(returnRequestId, files, userDetails.getMember());
        return ResponseEntity.ok().build();
    }

    /**
     * 현재 로그인한 사용자의 환불/반품 요청 목록을 페이징하여 조회합니다.
     *
     * @param pageable 페이징 정보 (페이지 번호, 페이지 크기, 정렬).
     * @return 페이징 처리된 나의 환불/반품 요청 요약 목록.
     */
    @GetMapping
    public ResponseEntity<Page<ReturnSummaryResponse>> getMyReturnRequests(
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Page<ReturnSummaryResponse> results = returnService.getMyReturnRequests(userDetails.getMember(), pageable);
        return ResponseEntity.ok(results);
    }

    /**
     * 나의 특정 환불/반품 요청 상세 정보를 조회합니다.
     *
     * @param returnRequestId 조회할 환불/반품 요청의 ID
     * @return 환불/반품 요청 상세 정보.
     */
    @GetMapping("/{returnRequestId}")
    public ResponseEntity<ReturnDetailResponse> getMyReturnRequestDetail(
            @PathVariable int returnRequestId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ReturnDetailResponse response = returnService.getMyReturnRequestDetail(returnRequestId, userDetails.getMember());
        return ResponseEntity.ok(response);
    }

    /**
     * 사용자가 직접 자신의 환불/반품 요청을 철회합니다.
     * <p>요청이 처리 중(IN_PROGRESS) 상태가 되기 전에만 철회할 수 있습니다.
     *
     * @param returnRequestId 철회할 환불/반품 요청의 ID
     * @return 작업 성공 시 204 No Content.
     */
    @PatchMapping("/{returnRequestId}/cancel")
    public ResponseEntity<Void> cancelReturnRequest(
            @PathVariable int returnRequestId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        returnService.cancelReturnRequest(returnRequestId, userDetails.getMember());
        return ResponseEntity.noContent().build();
    }
}
