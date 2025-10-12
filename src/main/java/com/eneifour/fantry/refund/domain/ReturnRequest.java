package com.eneifour.fantry.refund.domain;

import com.eneifour.fantry.auction.domain.OrderStatus;
import com.eneifour.fantry.auction.domain.Orders;
import com.eneifour.fantry.common.domain.BaseAuditingEntity;
import com.eneifour.fantry.common.util.file.FileMeta;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.refund.dto.ReturnAdminCreateRequest;
import com.eneifour.fantry.refund.dto.ReturnCreateRequest;
import com.eneifour.fantry.refund.exception.ReturnErrorCode;
import com.eneifour.fantry.refund.exception.ReturnException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 환불/반품 요청 정보를 나타내는 핵심 도메인 엔티티입니다.
 * 주문(Orders)과 연관되며, 요청의 상태 변경 이력을 관리합니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
@Table(name="return_requests")
public class ReturnRequest extends BaseAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int returnRequestId;

    // 환불/반품을 요청한 원본 주문
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders orders;

    // 환불/반품을 요청한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 환불/반품 사유 (Enum)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReturnReason reason;

    // 상세 사유
    @Column(name = "return_reason", columnDefinition = "TEXT")
    private String detailReason;

    // 현재 처리 상태 (Enum)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReturnStatus status;

    // 원본 주문의 결제 금액
    @Column(nullable = false)
    private BigDecimal originalPaymentAmount;

    // 반품 시 차감될 배송비
    private BigDecimal deductedShippingFee;

    // 최종 환불될 금액
    private BigDecimal finalRefundAmount;

    // 관리자가 환불/반품을 거절한 사유
    @Column(columnDefinition = "TEXT")
    private String rejectReason;

    // 환불/반품 처리가 완료된 시간
    private LocalDateTime completedAt;

    /** 관리자용 내부 메모 */
    @Column(length = 255)
    private String comment;

    // 요청 생성 주체 (사용자 또는 관리자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", updatable = false)
    private Member createdBy;

    // 마지막 수정 주체 (주로 관리자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private Member updatedBy;

    // 환불/반품 요청에 첨부된 파일 목록
    @Builder.Default
    @OneToMany(mappedBy = "returnRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReturnAttachment> attachments = new ArrayList<>();

    // 환불/반품 상태 변경 이력
    @Builder.Default
    @OneToMany(mappedBy = "returnRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReturnStatusHistory> statusHistories = new ArrayList<>();

    // --- 비즈니스 메서드 ---

    /** 환불 요청을 '승인' 상태로 변경하고, 최종 환불액을 계산합니다. */
    public void approve(BigDecimal deductedFee, Member modifier) {
        this.status = ReturnStatus.APPROVED;
        this.deductedShippingFee = (deductedFee != null) ? deductedFee : BigDecimal.ZERO;
        this.finalRefundAmount = this.originalPaymentAmount.subtract(this.deductedShippingFee);
        this.updatedBy = modifier;
    }

    /** 환불 요청을 '거절' 상태로 변경하고, 거절 사유를 기록합니다. */
    public void reject(String reason, Member modifier) {
        this.status = ReturnStatus.REJECTED;
        this.rejectReason = reason;
        this.updatedBy = modifier;
    }

    /** 환불 처리를 '완료' 상태로 변경하고, 완료 시간을 기록합니다. */
    public void complete(Member modifier) {
        this.status = ReturnStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.updatedBy = modifier;
    }

    /** 환불 상태를 주어진 값으로 단순 변경합니다. (내부 처리용) */
    public void changeStatus(ReturnStatus status, Member modifier) {
        this.status = status;
        this.updatedBy = modifier;
    }

    /** 환불 요청에 첨부파일을 추가합니다. */
    public void addAttachment(FileMeta fileMeta) {
        ReturnAttachment attachment = ReturnAttachment.builder()
                .returnRequest(this)
                .filemeta(fileMeta)
                .build();
        this.attachments.add(attachment);
    }

    /** 사용자가 환불 요청을 철회합니다. '요청됨' 상태에서만 가능합니다. */
    public void cancelByUser(Member modifier) {
        if (this.status != ReturnStatus.REQUESTED) {
            throw new ReturnException(ReturnErrorCode.CANCELLATION_NOT_ALLOWED);
        }
        this.status = ReturnStatus.USER_CANCELLED;
        this.updatedBy = modifier;
    }

    /** 환불 요청을 논리적으로 삭제 처리합니다. (상태 변경) */
    public void logicalDelete(Member modifier) {
        this.status = ReturnStatus.DELETED;
        this.updatedBy = modifier;
    }

    // --- 정적 팩토리 메서드 ---

    /**
     * 사용자의 환불 요청 DTO를 기반으로 ReturnRequest 엔티티를 생성합니다.
     * '배송 완료' 상태의 주문에 대해서만 생성이 가능합니다.
     */
    public static ReturnRequest of(Orders order, Member member, ReturnCreateRequest request) {
        if (order.getOrderStatus() != OrderStatus.DELIVERED) {
            throw new ReturnException(ReturnErrorCode.NOT_REFUNDABLE_STATUS);
        }
        return ReturnRequest.builder()
                .orders(order)
                .member(member)
                .reason(request.reason())
                .detailReason(request.detailReason())
                .status(ReturnStatus.REQUESTED)
                .originalPaymentAmount(BigDecimal.valueOf(order.getPrice()))
                .createdBy(member) // 사용자가 생성 주체
                .build();
    }

    /**
     * 관리자가 생성하는 환불 요청 DTO를 기반으로 ReturnRequest 엔티티를 생성합니다.
     * 생성 주체(createdBy)는 관리자로 기록됩니다.
     */
    public static ReturnRequest of(Orders order, Member buyer, ReturnAdminCreateRequest request, Member admin) {
        return ReturnRequest.builder()
                .orders(order)
                .member(buyer) // 환불 요청의 주체는 구매자(buyer)
                .reason(request.reason())
                .detailReason(request.detailReason())
                .status(ReturnStatus.REQUESTED)
                .originalPaymentAmount(BigDecimal.valueOf(order.getPrice()))
                .createdBy(admin) // 관리자가 생성 주체
                .build();
    }
}
