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
    ) {
        try {
            Payment createdPayment = paymentService.createPayment(paymentCreateRequest);
            PaymentResponse response = PaymentMapper.entityToResponse(createdPayment, PaymentResponse.class);
            return ResponseEntity.ok(new ApiResponse<>(true, response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/api/payments/{orderId}/verify")
    public ResponseEntity<ApiResponse<Payment>> requestPaymentVerify(
            @PathVariable("orderId") String orderId
    ) {
        Payment payment = paymentService.verifyPayment(orderId);
        return ResponseEntity.ok(new ApiResponse<>(payment.getStatus() == PaymentStatus.COMPLETE, payment, null));
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

    @PostMapping("/api/payments/{orderId}/cancel")
    public ResponseEntity<ApiResponse<PaymentCancelResponse>> requestPaymentCancel(
            @PathVariable("orderId") String orderId,
            @Valid
            @RequestBody
            PaymentCancelRequest paymentCancelRequest
    ) throws Exception {
        BootpayReceiptDto dto = paymentService.cancelPayment(orderId, paymentCancelRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, new PaymentCancelResponse()));
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
