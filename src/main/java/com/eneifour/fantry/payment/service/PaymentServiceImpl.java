package com.eneifour.fantry.payment.service;

import com.eneifour.fantry.payment.domain.Payment;
import com.eneifour.fantry.payment.domain.bootpay.BootpayReceiptDto;
import com.eneifour.fantry.payment.dto.PaymentCancelRequest;
import com.eneifour.fantry.payment.dto.PaymentCreateRequest;
import com.eneifour.fantry.payment.exception.*;
import com.eneifour.fantry.payment.mapper.PaymentMapper;
import com.eneifour.fantry.payment.repository.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * PaymentService의 구현체입니다.
 * <p>
 * 결제 생성, 승인, 취소, 검증 및 Webhook 처리 등의 핵심 비즈니스 로직을 구현합니다.
 * JPA의 낙관적 락(@Version)을 활용하여 동시성 문제를 제어합니다.
 * </p>
 *
 * @see PaymentService
 * @see Payment
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final GhostPaymentService ghostPaymentService;
    private final BootpayWebhookService bootpayWebhookService;
    private final BootpayService bootpayService;
    private final ObjectMapper objectMapper;

    /**
     * {@inheritDoc}
     * <p>
     * PaymentMapper를 통해 DTO를 엔티티로 변환하고 데이터베이스에 저장합니다.
     * </p>
     */
    @Override
    @Transactional
    public Payment createPayment(PaymentCreateRequest paymentCreateRequest) throws CreatePaymentFailedException {
        Payment payment;
        try {
            payment = PaymentMapper.requestToEntity(paymentCreateRequest);
            paymentRepository.save(payment);
        } catch (NoSuchAlgorithmException e) {
            throw new CreatePaymentFailedException(e);
        }
        return payment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BootpayReceiptDto getPreReceipt(String receiptId) throws Exception {
        return bootpayService.getReceiptViaWebClient(receiptId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Webhook 데이터를 파싱하여 webhook_type에 따라 적절한 핸들러 메서드를 호출합니다.
     * </p>
     */
    @Override
    @Transactional
    public void onBootpayWebhook(String webhookData) throws Exception {
        Map<String, Object> result = objectMapper.readValue(webhookData, new TypeReference<>() {
        });
        BootpayReceiptDto bootpayReceiptDto = objectMapper.convertValue(result, BootpayReceiptDto.class);
        switch (String.valueOf(result.get("webhook_type"))) {
            case "PAYMENT_COMPLETED" -> bootpayWebhookService.onPaymentComplete(bootpayReceiptDto);
            case "PAYMENT_CANCELLED" -> bootpayWebhookService.onPaymentCancelled(bootpayReceiptDto);
            case "PAYMENT_PARTIAL_CANCELLED" -> bootpayWebhookService.onPaymentPartialCancelled(bootpayReceiptDto);
            case "PAYMENT_CONFIRM_FAILED" -> bootpayWebhookService.onPaymentConfirmFailed(bootpayReceiptDto);
            case "PAYMENT_CANCEL_FAILED" -> bootpayWebhookService.onPaymentCancelFailed(bootpayReceiptDto);
            case "PAYMENT_REQUEST_FAILED" -> bootpayWebhookService.onPaymentRequestFailed(bootpayReceiptDto);
            case "PAYMENT_EXPIRED" -> bootpayWebhookService.onPaymentExpired(result);
            default -> bootpayWebhookService.onError(result);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Payment verifyPayment(String orderId) {
        return paymentRepository.findByOrderId(orderId).orElseThrow(NotFoundPaymentException::new);
    }

    /**
     * {@inheritDoc}
     * <p>
     * 낙관적 락(@Version)을 사용하여 동시성을 제어합니다.
     * ObjectOptimisticLockingFailureException 발생 시 유령 결제로 등록합니다.
     * </p>
     *
     * @throws ConcurrentPaymentException 동시성 문제 발생 시
     */
    @Override
    @Transactional
    public void purchaseItem(String orderId, String data) throws JsonProcessingException {
        BootpayReceiptDto receiptFromClient = objectMapper.readValue(data, BootpayReceiptDto.class);
        try {
            Payment payment = paymentRepository.findByOrderId(receiptFromClient.getOrderId())
                    .orElseThrow(NotFoundPaymentException::new);

            BootpayReceiptDto receiptFromBootpay = bootpayService.getReceiptViaWebClient(receiptFromClient.getReceiptId());
            bootpayWebhookService.processPaymentVerification(payment, receiptFromBootpay);
        } catch (ObjectOptimisticLockingFailureException | BootpayException e) {
            ghostPaymentService.createGhostPayment(receiptFromClient.getReceiptId());
            throw new ConcurrentPaymentException(e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Bootpay 취소 API를 호출한 후 로컬 데이터베이스의 결제 정보를 업데이트합니다.
     * </p>
     */
    @Override
    @Transactional
    public BootpayReceiptDto cancelPayment(String orderId, PaymentCancelRequest paymentCancelRequest) {
        Payment payment = paymentRepository.findByOrderId(paymentCancelRequest.getOrderId())
                .orElseThrow(NotFoundReceiptException::new);

        if(!orderId.equals(payment.getOrderId())) {
            throw new OrderIdMismatchException();
        }

        BootpayReceiptDto resultReceipt = bootpayService.getReceiptViaWebClient(payment.getReceiptId());
        BootpayReceiptDto cancelResult = bootpayService.cancellationViaWebClient(payment.getReceiptId(), paymentCancelRequest.getCancelReason(), paymentCancelRequest.getUsername(), resultReceipt.getOrderId(), paymentCancelRequest.getCancelPrice(), paymentCancelRequest.getBankDataDto());
        PaymentMapper.updateFromDto(payment, cancelResult);
        paymentRepository.save(payment);
        return cancelResult;
    }

    @Override
    public BootpayReceiptDto voidPayment(String receiptId) {
        BootpayReceiptDto resultReceipt = bootpayService.getReceiptViaWebClient(receiptId);
        return bootpayService.cancellationViaWebClient(receiptId, "결제 무효처리", "FANTRY_SYSTEM", resultReceipt.getOrderId(), String.valueOf(resultReceipt.getPrice()), resultReceipt.getBankDataDto());
    }
}
