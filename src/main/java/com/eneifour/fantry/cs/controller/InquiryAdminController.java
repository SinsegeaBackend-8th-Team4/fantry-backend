package com.eneifour.fantry.cs.controller;

import com.eneifour.fantry.cs.dto.InquiryAnswerRequest;
import com.eneifour.fantry.cs.dto.InquiryDetailAdminResponse;
import com.eneifour.fantry.cs.dto.InquirySearchCondition;
import com.eneifour.fantry.cs.dto.InquirySummaryResponse;
import com.eneifour.fantry.cs.service.InquiryService;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 관리자용 1:1 문의 관련 API를 제공하는 컨트롤러입니다.
 * <p>모든 API는 관리자 권한을 가진 사용자만 접근할 수 있습니다.
 *
 * @author 정재환
 * @since 2025.10.11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/cs/inquiry")
public class InquiryAdminController {

    private final InquiryService inquiryService;

    /**
     * 1:1 문의 목록을 검색 조건에 따라 페이징하여 조회합니다.
     * <p>검색 조건(처리 상태, 문의 유형, 사용자 이름)은 선택 사항이며, 조건이 없으면 전체 목록이 조회됩니다.
     *
     * @param condition 검색 조건 DTO.
     *                  <p><b>[검색 가능한 처리 상태(status)]</b></p>
     *                  <ul>
     *                      <li>PENDING: 답변 대기</li>
     *                      <li>IN_PROGRESS: 처리 중</li>
     *                      <li>ON_HOLD: 보류</li>
     *                      <li>REJECTED: 거절</li>
     *                      <li>ANSWERED: 답변 완료</li>
     *                  </ul>
     *                  <p><b>[문의 유형(csType) ID]</b></p>
     *                  <ul>
     *                      <li>1: 배송문의</li>
     *                      <li>2: 결제문의</li>
     *                      <li>3: 기타문의</li>
     *                      <li>4: 상품문의</li>
     *                      <li>5: 환불/반품 문의</li>
     *                      <li>6: 판매 문의</li>
     *                  </ul>
     * @param pageable  페이징 정보 (페이지 번호, 페이지 크기, 정렬).
     * @return 페이징 처리된 1:1 문의 요약 목록.
     */
    @GetMapping
    public ResponseEntity<Page<InquirySummaryResponse>> searchInquiries(
            @ModelAttribute InquirySearchCondition condition,
            Pageable pageable
    ) {
        Page<InquirySummaryResponse> results = inquiryService.searchInquires(condition, pageable);
        return ResponseEntity.ok(results);
    }

    /**
     * 특정 1:1 문의의 상세 내용을 관리자용으로 조회합니다.
     * <p>사용자용 조회와 달리 답변 작성을 위한 추가 정보가 포함될 수 있습니다.
     *
     * @param inquiryId 조회할 문의의 ID.
     * @return 관리자용 문의 상세 정보.
     */
    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryDetailAdminResponse> getInquiryDetail(
            @PathVariable int inquiryId
    ) {
        InquiryDetailAdminResponse inquiry = inquiryService.getInquiryForAdmin(inquiryId);
        return ResponseEntity.ok(inquiry);
    }

    /**
     * 특정 1:1 문의에 답변을 등록하고 처리 상태를 변경합니다.
     *
     * @param inquiryId     답변할 문의의 ID.
     * @param answerRequest 답변 내용 및 변경할 상태 정보를 담는 DTO.
     *                      <p><b>[변경 가능한 처리 상태(reqStatus)]</b></p>
     *                      <ul>
     *                          <li>PENDING: 답변 대기</li>
     *                          <li>IN_PROGRESS: 처리 중</li>
     *                          <li>ON_HOLD: 보류</li>
     *                          <li>REJECTED: 거절</li>
     *                          <li>ANSWERED: 답변 완료</li>
     *                      </ul>
     * @return 답변이 등록되고 상태가 변경된 후의 문의 상세 정보.
     */
    @PatchMapping("/{inquiryId}/answer")
    public ResponseEntity<InquiryDetailAdminResponse> answerInquiry(
            @PathVariable int inquiryId,
            @RequestBody InquiryAnswerRequest answerRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member admin = userDetails.getMember();
        InquiryDetailAdminResponse updatedInquiry = inquiryService.answerInquiry(inquiryId, answerRequest, admin);
        return ResponseEntity.ok(updatedInquiry);
    }
}
