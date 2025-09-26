package com.eneifour.fantry.payment.mapper;

import com.eneifour.fantry.payment.domain.Payment;
import com.eneifour.fantry.payment.domain.bootpay.BootpayReceiptDto;
import com.eneifour.fantry.payment.domain.request.RequestPaymentCreate;
import com.eneifour.fantry.payment.domain.response.ResponseCreatedPayment;
import com.eneifour.fantry.payment.domain.response.ResponsePaymentReceipt;
import com.eneifour.fantry.payment.util.Encryptor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.NoSuchAlgorithmException;

public class PaymentMapper {
    public static Payment requestToEntity(RequestPaymentCreate requestPaymentCreate) throws NoSuchAlgorithmException {
        Integer itemId = Integer.parseInt(requestPaymentCreate.getItemId());
        Integer price = Integer.parseInt(requestPaymentCreate.getPrice());
        String orderId = Encryptor.createOrderId(requestPaymentCreate.getMemberId(), itemId);
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setPrice(price);
        payment.setStatus(100);
        return payment;
    }

    public static Payment dtoToEntity(BootpayReceiptDto bootpayReceiptDto) {
        Payment payment = new Payment();
        payment.setReceiptId(bootpayReceiptDto.getReceiptId());
        payment.setOrderId(bootpayReceiptDto.getOrderId());
        payment.setPrice(bootpayReceiptDto.getPrice());
        payment.setCancelledPrice(bootpayReceiptDto.getCancelledPrice());
        payment.setOrderName(bootpayReceiptDto.getOrderName());
        payment.setMetadata(bootpayReceiptDto.getMetadata().toString());
        payment.setPg(bootpayReceiptDto.getPg());
        payment.setMethod(bootpayReceiptDto.getMethod());
        payment.setCurrency(bootpayReceiptDto.getCurrency());
        payment.setRequestedAt(bootpayReceiptDto.getRequestedAt());
        payment.setPurchasedAt(bootpayReceiptDto.getPurchasedAt());
        payment.setCancelledAt(bootpayReceiptDto.getCancelledAt());
        payment.setReceiptUrl(bootpayReceiptDto.getReceiptUrl());
        payment.setStatus(bootpayReceiptDto.getStatus());
        ObjectMapper objectMapper = new ObjectMapper();
        Object paymentData = null;
        if (bootpayReceiptDto.getCardDataDto() != null) {
            paymentData = bootpayReceiptDto.getCardDataDto();
        } else if (bootpayReceiptDto.getBankDataDto() != null) {
            paymentData = bootpayReceiptDto.getBankDataDto();
        } else if (bootpayReceiptDto.getVirtualBankDataDto() != null) {
            paymentData = bootpayReceiptDto.getVirtualBankDataDto();
        }
        if (paymentData != null) {
            payment.setPaymentInfo(objectMapper.convertValue(paymentData, new TypeReference<>() {
            }));
        }

        return payment;
    }

    public static <T> T entityToResponse(Payment payment, Class<T> clazz) throws ClassNotFoundException {
        if (clazz == ResponsePaymentReceipt.class) {
            ResponsePaymentReceipt responsePaymentReceipt = new ResponsePaymentReceipt(payment.getReceiptId());
            return clazz.cast(responsePaymentReceipt);
        } else if (clazz == ResponseCreatedPayment.class) {
            ResponseCreatedPayment responseCreatedPayment = new ResponseCreatedPayment(payment.getOrderId());
            return clazz.cast(responseCreatedPayment);
        } else {
            throw new ClassNotFoundException();
        }
    }
}
