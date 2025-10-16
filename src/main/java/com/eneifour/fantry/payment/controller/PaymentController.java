package com.eneifour.fantry.payment.controller;

import com.eneifour.fantry.payment.domain.Payment;
import com.eneifour.fantry.payment.domain.PaymentStatus;
import com.eneifour.fantry.payment.domain.bootpay.BootpayReceiptDto;
import com.eneifour.fantry.payment.dto.*;
import com.eneifour.fantry.payment.mapper.PaymentMapper;
import com.eneifour.fantry.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/api/payments")
    public ResponseEntity<ApiResponse<PaymentResponse>> requestPaymentCreate(
            @Valid
            @RequestBody
            PaymentCreateRequest paymentCreateRequest
    ) throws NoSuchAlgorithmException, ClassNotFoundException {
        Payment createdPayment = paymentService.createPayment(paymentCreateRequest);
        PaymentResponse response = PaymentMapper.entityToResponse(createdPayment, PaymentResponse.class);
        return ResponseEntity.ok(new ApiResponse<>(true, response));
    }

    @PostMapping("/api/payments/{orderId}/verify")
    public ResponseEntity<ApiResponse<Payment>> requestPaymentVerify(
            @PathVariable("orderId") String orderId,
            @RequestBody PaymentVerifyRequest paymentVerifyRequest
    ) {
        PaymentStatus paymentStatus = PaymentStatus.fromCode(paymentVerifyRequest.getPaymentStatus());
        Payment payment = paymentService.verifyPayment(orderId);
        boolean verified;
        switch (paymentStatus) {
            case COMPLETE -> {
                verified = payment.getStatus() == PaymentStatus.COMPLETE;
            }
            case RETURNED -> {
                verified = payment.getStatus() == PaymentStatus.RETURNED;
            }
            case CANCELED -> {
                verified = payment.getStatus() == PaymentStatus.CANCELED;
            }
            default -> throw new IllegalArgumentException("잘 못된 상태값 입니다.");
        }

        return ResponseEntity.ok(new ApiResponse<>(verified, payment, null));
    }

    @PostMapping("/api/payments/{orderId}/approve")
    public ResponseEntity<ApiResponse<String>> requestPaymentApprove(
            @PathVariable("orderId") String orderId,
            @Valid
            @RequestBody
            PaymentApproveRequest paymentApproveRequest
    ) throws Exception {
        log.info("PaymentApproveDto : {}", paymentApproveRequest);
        paymentService.purchaseItem(orderId, paymentApproveRequest.getReceiptData());
        return ResponseEntity.ok(new ApiResponse<>(true, "결제가 완료되었습니다.", null));
    }

    @PostMapping("/api/payments/{receiptId}/void")
    public ResponseEntity<ApiResponse<PaymentCancelResponse>> requestPaymentCancel(
            @PathVariable("receiptId") String receiptId
    ) throws Exception {
        BootpayReceiptDto dto = paymentService.voidPayment(receiptId);
        PaymentCancelResponse response = PaymentCancelResponse.from(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, response));
    }

    @PostMapping("/webhook/bootpay")
    public ResponseEntity<?> onReceiveWebhook(
            @RequestBody String webhook
    ) throws Exception {
        paymentService.onBootpayWebhook(webhook);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse<String>> handleException(MethodArgumentNotValidException methodArgumentNotValidException) {
        Map<String, String> errors = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, errors.values().toString()));
    }
}
