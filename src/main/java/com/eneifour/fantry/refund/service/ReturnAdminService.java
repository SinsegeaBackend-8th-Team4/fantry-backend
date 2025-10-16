package com.eneifour.fantry.refund.service;

import com.eneifour.fantry.common.util.file.FileService;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.repository.JpaMemberRepository;
import com.eneifour.fantry.orders.domain.Orders;
import com.eneifour.fantry.payment.domain.Payment;
import com.eneifour.fantry.payment.dto.PaymentCancelRequest;
import com.eneifour.fantry.payment.repository.PaymentRepository;
import com.eneifour.fantry.payment.service.PaymentService;
import com.eneifour.fantry.refund.domain.ReturnRequest;
import com.eneifour.fantry.refund.domain.ReturnStatus;
import com.eneifour.fantry.refund.domain.ReturnStatusHistory;
import com.eneifour.fantry.refund.dto.*;
import com.eneifour.fantry.refund.exception.ReturnErrorCode;
import com.eneifour.fantry.refund.exception.ReturnException;
import com.eneifour.fantry.refund.repository.ReturnRepository;
import com.eneifour.fantry.refund.repository.ReturnStatusHistoryRepository;
import com.eneifour.fantry.orders.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ReturnAdminService {

    private final ReturnRepository returnRepository;
    private final ReturnSpecification returnSpecification;
    private final JpaMemberRepository memberRepository;
    private final PaymentRepository paymentRepository;
    private final OrdersRepository ordersRepository;
    private final ReturnStatusHistoryRepository historyRepository;
    private final PaymentService paymentService;
    private final FileService fileService;

    public ReturnAdminResponse createReturnRequestByAdmin(ReturnAdminCreateRequest request, Member admin) {
        Member buyer = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new ReturnException(ReturnErrorCode.BUYER_NOT_FOUND));

        Payment payment = paymentRepository.findByOrderId(request.orderId())
                .orElseThrow(() -> new ReturnException(ReturnErrorCode.ORDER_NOT_FOUND));

        Orders order = ordersRepository.findByPayment(payment)
                .orElseThrow(() -> new ReturnException(ReturnErrorCode.ORDER_NOT_FOUND));

        if (returnRepository.existsByOrders(order)) {
            throw new ReturnException(ReturnErrorCode.DUPLICATE_REQUEST);
        }

        ReturnRequest returnRequest = ReturnRequest.of(order, buyer, request, admin);
        ReturnRequest savedReturnRequest = returnRepository.save(returnRequest);
        return ReturnAdminResponse.from(savedReturnRequest, Collections.emptyList());
    }

    public void deleteReturnRequest(int returnRequestId, Member admin) {
        ReturnRequest returnRequest = returnRepository.findById(returnRequestId)
                .orElseThrow(() -> new ReturnException(ReturnErrorCode.RETURN_REQUEST_NOT_FOUND));

        ReturnStatus oldStatus = returnRequest.getStatus();
        returnRequest.logicalDelete(admin);
        addStatusHistory(returnRequest, oldStatus, ReturnStatus.DELETED, admin, "관리자가 논리적으로 삭제 처리함");
    }

    public ReturnAdminResponse updateReturnRequestStatus(int returnRequestId, ReturnAdminUpdateRequest request, Member admin) {
        ReturnRequest returnRequest = returnRepository.findWithAttachmentsAndHistoriesById(returnRequestId)
                .orElseThrow(() -> new ReturnException(ReturnErrorCode.RETURN_REQUEST_NOT_FOUND));

        ReturnStatus oldStatus = returnRequest.getStatus();

        switch (request.status()) {
            case APPROVED -> processApproval(returnRequest, request, admin, oldStatus);
            case REJECTED -> processRejection(returnRequest, request, admin, oldStatus);
            case IN_TRANSIT, INSPECTING -> processStatusChange(returnRequest, request, admin, oldStatus);
            default -> throw new ReturnException(ReturnErrorCode.STATUS_NOT_SUPPORTED);
        }

        List<String> urls = getAttachmentUrls(returnRequest);
        return ReturnAdminResponse.from(returnRequest, urls);
    }

    @Transactional(readOnly = true)
    public Page<ReturnSummaryResponse> searchReturnRequests(ReturnSearchRequest request, Pageable pageable) {
        Specification<ReturnRequest> spec = returnSpecification.toSpecification(request);
        Page<ReturnRequest> returnRequestPage = returnRepository.findAll(spec, pageable);
        return returnRequestPage.map(ReturnSummaryResponse::from);
    }

    @Transactional(readOnly = true)
    public ReturnAdminResponse getReturnRequestDetail(int returnRequestId) {
        ReturnRequest returnRequest = returnRepository.findWithAttachmentsAndHistoriesById(returnRequestId)
                .orElseThrow(() -> new ReturnException(ReturnErrorCode.RETURN_REQUEST_NOT_FOUND));

        List<String> urls = getAttachmentUrls(returnRequest);
        return ReturnAdminResponse.from(returnRequest, urls);
    }

    // --- Helper Methods ---

    private void addStatusHistory(ReturnRequest returnRequest, ReturnStatus oldStatus, ReturnStatus newStatus, Member updatedBy, String memo) {
        ReturnStatusHistory history = ReturnStatusHistory.builder()
                .returnRequest(returnRequest)
                .previousStatus(oldStatus)
                .newStatus(newStatus)
                .updatedBy(updatedBy)
                .memo(memo)
                .build();
        historyRepository.save(history);
    }

    private void processApproval(ReturnRequest returnRequest, ReturnAdminUpdateRequest request, Member admin, ReturnStatus oldStatus) {
        returnRequest.approve(request.deductedShippingFee(), admin);
        cancelPaymentForRequest(returnRequest);
        returnRequest.complete(admin);
        addStatusHistory(returnRequest, oldStatus, ReturnStatus.APPROVED, admin, request.memo());
        addStatusHistory(returnRequest, ReturnStatus.APPROVED, ReturnStatus.COMPLETED, admin, "결제 환불 처리 완료");
    }

    private void processRejection(ReturnRequest returnRequest, ReturnAdminUpdateRequest request, Member admin, ReturnStatus oldStatus) {
        returnRequest.reject(request.rejectReason(), admin);
        addStatusHistory(returnRequest, oldStatus, request.status(), admin, request.memo());
    }

    private void processStatusChange(ReturnRequest returnRequest, ReturnAdminUpdateRequest request, Member admin, ReturnStatus oldStatus) {
        returnRequest.changeStatus(request.status(), admin);
        addStatusHistory(returnRequest, oldStatus, request.status(), admin, request.memo());
    }

    private void cancelPaymentForRequest(ReturnRequest returnRequest) {
        try {
            String orderId = returnRequest.getOrders().getPayment().getOrderId();
            if (orderId == null) throw new ReturnException(ReturnErrorCode.PAYMENT_INFO_NOT_FOUND);

            PaymentCancelRequest cancelRequest = PaymentCancelRequest.builder()
                    .orderId(orderId)
                    .cancelPrice(returnRequest.getFinalRefundAmount().toString())
                    .username(String.valueOf(returnRequest.getMember().getMemberId()))
                    .cancelReason("관리자 환불 승인")
                    .build();
            paymentService.cancelPayment(returnRequest.getOrders().getPayment().getOrderId(), cancelRequest);
        } catch (Exception e) {
            throw new ReturnException(ReturnErrorCode.REFUND_FAILED, e);
        }
    }

    private List<String> getAttachmentUrls(ReturnRequest returnRequest) {
        if (returnRequest.getAttachments() == null || returnRequest.getAttachments().isEmpty()) {
            return new ArrayList<>();
        }
        List<Integer> fileMetaIds = returnRequest.getAttachments().stream()
                .map(attachment -> attachment.getFilemeta().getFilemetaId())
                .toList();
        Map<Integer, String> urlMap = fileService.getFileAccessUrls(fileMetaIds);
        return new ArrayList<>(urlMap.values());
    }
}
