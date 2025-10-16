package com.eneifour.fantry.payment.mapper;

import com.eneifour.fantry.payment.domain.Payment;
import com.eneifour.fantry.payment.domain.PaymentStatus;
import com.eneifour.fantry.payment.domain.bootpay.BootPayStatus;
import com.eneifour.fantry.payment.domain.bootpay.BootpayReceiptDto;
import com.eneifour.fantry.payment.dto.PaymentCreateRequest;
import com.eneifour.fantry.payment.dto.PaymentResponse;
import com.eneifour.fantry.payment.util.Encryptor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
public class PaymentMapper {

    public static Payment requestToEntity(PaymentCreateRequest paymentCreateRequest) throws NoSuchAlgorithmException {
        Integer itemId = Integer.parseInt(paymentCreateRequest.getItemId());
        Integer price = Integer.parseInt(paymentCreateRequest.getPrice());
        String orderId = Encryptor.createOrderId(paymentCreateRequest.getUsername(), itemId);
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setPrice(price);
        return payment;
    }

    public static <T> T entityToResponse(Payment payment, Class<T> clazz) throws ClassNotFoundException {
        if (clazz == PaymentResponse.class) {
            PaymentResponse paymentResponse = new PaymentResponse(payment.getOrderId());
            return clazz.cast(paymentResponse);
        } else {
            throw new ClassNotFoundException();
        }
    }

    public static void updateFromDto(Payment payment, BootpayReceiptDto dto) {
        payment.setReceiptId(dto.getReceiptId());
        payment.setCancelledPrice(dto.getCancelledPrice());
        payment.setOrderName(dto.getOrderName());
        payment.setPg(dto.getPg());
        payment.setMethod(dto.getMethod());
        payment.setCurrency(dto.getCurrency());
        payment.setRequestedAt(dto.getRequestedAt().toLocalDateTime());
        payment.setPurchasedAt(dto.getPurchasedAt().toLocalDateTime());
        if (dto.getCancelledAt() != null) {
            payment.setCancelledAt(dto.getCancelledAt().toLocalDateTime());
        }
        payment.setReceiptUrl(dto.getReceiptUrl());
        payment.setBootpayStatus(BootPayStatus.fromCode(dto.getStatus()));

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            payment.setMetadata(objectMapper.writeValueAsString(dto.getMetadata()));
        } catch (Exception e) {
            payment.setMetadata(null);
        }

        Object paymentData = null;
        if (dto.getCardDataDto() != null) {
            paymentData = dto.getCardDataDto();
        } else if (dto.getBankDataDto() != null) {
            paymentData = dto.getBankDataDto();
        } else if (dto.getVirtualBankDataDto() != null) {
            paymentData = dto.getVirtualBankDataDto();
        }

        if (paymentData != null) {
            payment.setPaymentInfo(objectMapper.convertValue(paymentData, new TypeReference<>() {
            }));
        }

        if (payment.getBootpayStatus() == BootPayStatus.PAYMENT_COMPLETED && payment.getCancelledPrice() > 0) {
            payment.setStatus(PaymentStatus.RETURNED);
        } else if (payment.getBootpayStatus() == BootPayStatus.PAYMENT_CANCELLED) {
            payment.setStatus(PaymentStatus.CANCELED);
        }
    }
}
