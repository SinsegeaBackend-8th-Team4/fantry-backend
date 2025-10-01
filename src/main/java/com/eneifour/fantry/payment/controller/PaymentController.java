package com.eneifour.fantry.payment.controller;

import com.eneifour.fantry.payment.Constants;
import com.eneifour.fantry.payment.domain.Payment;
import com.eneifour.fantry.payment.domain.request.RequestPaymentApprove;
import com.eneifour.fantry.payment.domain.request.RequestPaymentCancel;
import com.eneifour.fantry.payment.domain.request.RequestPaymentCreate;
import com.eneifour.fantry.payment.domain.response.ApiResponse;
import com.eneifour.fantry.payment.domain.response.ResponseCreatedPayment;
import com.eneifour.fantry.payment.mapper.PaymentMapper;
import com.eneifour.fantry.payment.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RestController
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/api/payment/history/{userId}/{pageNumber}")
    public ResponseEntity<List<Payment>> getUserPayments(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("userId") Integer userId,
            @PathVariable(value = "pageNumber", required = false) Integer pageNumber
    ) {
        int page = 0;
        if (pageNumber != null && pageNumber > 0) {
            page = pageNumber - 1;
        }
        return null;
    }

    @GetMapping("/api/payment/history/{pageNumber}")
    public ResponseEntity<ApiResponse<List<Payment>>> getHistory(
            @AuthenticationPrincipal UserDetails userDetails,
            @Min(1)
            @PathVariable(value = "pageNumber") Integer pageNumber,
            @RequestParam(value = "sort", defaultValue = "DESC") String sort,
            @Min(1)
            @Max(100)
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize
    ) {
        if (pageNumber < 1) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "페이지 번호는 1 이상이어야 합니다."));
        }

        String sortDirection = sort.toUpperCase(Locale.ROOT);
        if (!Constants.Sort.ASC.equals(sortDirection) && !Constants.Sort.DESC.equals(sortDirection)) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "정렬 방향은 ASC 또는 DESC만 가능합니다."));
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        try {
            List<Payment> payments = paymentService.getPayment(pageable, Constants.Sort.DESC.equals(sortDirection));
            if (payments.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse<>(true, List.of()));
            } else {
                return ResponseEntity.ok(new ApiResponse<>(true, payments));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "서버에 문제가 발생하였습니다."));
        }
    }

    @PostMapping("/api/payment/create")
    public ResponseEntity<ApiResponse<ResponseCreatedPayment>> requestPaymentCreate(
            @Valid
            @RequestBody
            RequestPaymentCreate requestPaymentCreate
    ) {
        try {
            Payment createdPayment = paymentService.createPayment(requestPaymentCreate);
            ResponseCreatedPayment response = PaymentMapper.entityToResponse(createdPayment, ResponseCreatedPayment.class);
            return ResponseEntity.ok(new ApiResponse<>(true, response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/api/payment/approve")
    public ResponseEntity<ApiResponse<String>> requestPaymentApprove(
            @Valid
            @RequestBody
            RequestPaymentApprove requestPaymentApprove
    ) throws Exception {
        log.info("RequestPaymentApprove : {}", requestPaymentApprove);
        paymentService.purchaseItem(requestPaymentApprove);
        return ResponseEntity.ok(new ApiResponse<>(true, "결제가 완료되었습니다.", null));
    }

    @PostMapping("/api/payment/cancel")
    public ResponseEntity<Void> requestPaymentCancel(
            @Valid
            @RequestBody
            RequestPaymentCancel requestPaymentCancel
    ) throws Exception {
        paymentService.cancelPayment(requestPaymentCancel);
        return null;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> onReceiveWebhook(
            @RequestBody String webhook
    ) throws Exception {
        log.info(webhook);
        paymentService.handleWebhook(webhook);
        return ResponseEntity.ok("{\"success\":true}");
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
