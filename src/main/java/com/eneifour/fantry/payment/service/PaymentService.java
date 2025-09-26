package com.eneifour.fantry.payment.service;

import com.eneifour.fantry.payment.domain.Payment;
import com.eneifour.fantry.payment.domain.bootpay.BootpayReceiptDto;
import com.eneifour.fantry.payment.domain.request.RequestPaymentApprove;
import com.eneifour.fantry.payment.domain.request.RequestPaymentCancel;
import com.eneifour.fantry.payment.domain.request.RequestPaymentCreate;
import org.springframework.data.domain.Pageable;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface PaymentService {
    Payment createPayment(RequestPaymentCreate requestPaymentCreate) throws NoSuchAlgorithmException;

    List<Payment> getPayment(Pageable pageable, boolean desanding);

    List<Payment> getPayment(Integer integer, Pageable pageable);

    void purchaseItem(RequestPaymentApprove requestPaymentApprove) throws Exception;

    void cancelPayment(RequestPaymentCancel requestPaymentCancel) throws Exception;

    BootpayReceiptDto getPreReceipt(String receiptId) throws Exception;

    void handleWebhook(String webhookData) throws Exception;
}
