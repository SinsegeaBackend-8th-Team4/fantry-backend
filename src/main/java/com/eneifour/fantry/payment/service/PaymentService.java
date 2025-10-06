package com.eneifour.fantry.payment.service;

import com.eneifour.fantry.payment.domain.Payment;
import com.eneifour.fantry.payment.domain.bootpay.BootpayReceiptDto;
import com.eneifour.fantry.payment.dto.PaymentCancelRequestDto;
import com.eneifour.fantry.payment.dto.PaymentCreateRequestDto;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface PaymentService {
    /**
     * 새로운 결제를 생성합니다.
     * <p>
     * 클라이언트로부터 받은 결제 정보를 기반으로 Payment 엔티티를 생성하고 저장합니다.
     * </p>
     *
     * @param paymentCreateRequestDto 결제 생성 요청 정보
     * @return 생성된 Payment 엔티티
     * @throws NoSuchAlgorithmException 암호화 알고리즘 오류 시
     */
    Payment createPayment(PaymentCreateRequestDto paymentCreateRequestDto) throws NoSuchAlgorithmException;

    List<Payment> getPayment(Integer integer, Pageable pageable);

    /**
     * 결제를 취소합니다.
     * <p>
     * Bootpay를 통해 결제 취소를 요청하고, 취소 결과를 반환합니다.
     * </p>
     *
     * @param paymentCancelRequestDto 결제 취소 요청 정보
     * @return 취소된 결제의 영수증 정보
     * @throws Exception Bootpay API 호출 실패 시
     */
    BootpayReceiptDto cancelPayment(PaymentCancelRequestDto paymentCancelRequestDto) throws Exception;

    void cancelPayment(RequestPaymentCancel requestPaymentCancel) throws Exception;

    BootpayReceiptDto getPreReceipt(String receiptId) throws Exception;

    void handleWebhook(String webhookData) throws Exception;
}
