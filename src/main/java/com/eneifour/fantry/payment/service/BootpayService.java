package com.eneifour.fantry.payment.service;

import com.eneifour.fantry.payment.domain.bootpay.BankDataDto;
import com.eneifour.fantry.payment.domain.bootpay.BootpayReceiptDto;
import com.eneifour.fantry.payment.util.BootpayValidator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.bootpay.pg.Bootpay;
import kr.co.bootpay.pg.model.request.Cancel;
import kr.co.bootpay.pg.model.request.RefundData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class BootpayService {
    private final Bootpay bootpay;
    private final BootpayValidator bootpayValidator;
    private final ObjectMapper objectMapper;

    public BootpayService(
            Bootpay bootpay,
            BootpayValidator bootpayValidator,
            ObjectMapper objectMapper
    ) {
        this.bootpay = bootpay;
        this.bootpayValidator = bootpayValidator;
        this.objectMapper = objectMapper;
    }

    /**
     * Bootpay 영수증 정보를 조회하고 검증합니다.
     *
     * @param receiptId 영수증 ID
     * @return 조회된 영수증 정보를 담은 BootpayReceiptDto 객체
     * @throws Exception Bootpay API 호출 또는 검증 실패 시 발생
     */
    public BootpayReceiptDto getReceipt(String receiptId) throws Exception {
        Map<String, Object> resultReceipt = bootpay.getReceipt(receiptId);
        bootpayValidator.checkBootpayReceipt(resultReceipt);
        return objectMapper.convertValue(resultReceipt, BootpayReceiptDto.class);
    }

    /**
     * Bootpay 결제를 승인하고 응답을 검증합니다.
     *
     * @param receiptId 승인할 영수증 ID
     * @return 승인된 영수증 정보를 담은 BootpayReceiptDto 객체
     * @throws Exception Bootpay API 호출 또는 검증 실패 시 발생
     */
    public BootpayReceiptDto approve(String receiptId) throws Exception {
        Map<String, Object> result = bootpay.confirm(receiptId);
        bootpayValidator.checkBootpayReceipt(result);
        return objectMapper.convertValue(result, BootpayReceiptDto.class);
    }

    /**
     * Bootpay 결제를 취소합니다.
     *
     * @param receiptId    취소할 영수증 ID
     * @param cancelReason 취소 사유
     * @param adminId      취소를 요청한 관리자 ID
     * @param orderId      취소 ID
     * @param cancelPrice  취소 금액
     * @param bankDataDto  환불 계좌 정보 (가상 계좌이체 환불 시 필요)
     * @return 취소된 영수증 정보를 담은 BootpayReceiptDto 객체
     * @throws Exception Bootpay API 호출 또는 검증 실패 시 발생
     */
    public BootpayReceiptDto cancellation(String receiptId, String cancelReason, String adminId, String orderId, String cancelPrice, BankDataDto bankDataDto) throws Exception {
        Cancel cancel = new Cancel();
        cancel.receiptId = receiptId;
        cancel.cancelMessage = cancelReason;
        cancel.cancelUsername = adminId;
        cancel.cancelPrice = Double.parseDouble(cancelPrice);
        cancel.cancelId = orderId;
        if (bankDataDto != null) {
            RefundData refundData = new RefundData();
            refundData.bankAccount = bankDataDto.getBankAccount();
            refundData.bankCode = bankDataDto.getBankCode();
            refundData.bankUsername = bankDataDto.getBankUsername();
            cancel.refund = refundData;
        }
        Map<String, Object> result = bootpay.receiptCancel(cancel);
        bootpayValidator.checkBootpayReceipt(result);
        return objectMapper.convertValue(result, BootpayReceiptDto.class);
    }

    public BootpayReceiptDto convertWebhook(String webhookData) throws Exception {
        Map<String, Object> webhook = objectMapper.readValue(webhookData, new TypeReference<>() {
        });
        bootpayValidator.checkBootpayReceipt(webhook);
        return objectMapper.convertValue(webhook, BootpayReceiptDto.class);
    }
}