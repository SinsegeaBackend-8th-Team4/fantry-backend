package com.eneifour.fantry.payment.service;

import com.eneifour.fantry.payment.domain.Payment;
import com.eneifour.fantry.payment.domain.PaymentStatus;
import com.eneifour.fantry.payment.domain.bootpay.BootpayReceiptDto;
import com.eneifour.fantry.payment.dto.PaymentCancelRequest;
import com.eneifour.fantry.payment.dto.PaymentCreateRequest;

import java.security.NoSuchAlgorithmException;

/**
 * 결제 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 * <p>
 * 결제 생성, 승인, 취소, 검증 및 Webhook 처리 등의 핵심 결제 기능을 정의합니다.
 * </p>
 *
 * @see PaymentServiceImpl
 */
public interface PaymentService {
    /**
     * 새로운 결제를 생성합니다.
     * <p>
     * 클라이언트로부터 받은 결제 정보를 기반으로 Payment 엔티티를 생성하고 저장합니다.
     * </p>
     *
     * @param paymentCreateRequest 결제 생성 요청 정보
     * @return 생성된 Payment 엔티티
     * @throws NoSuchAlgorithmException 암호화 알고리즘 오류 시
     */
    Payment createPayment(PaymentCreateRequest paymentCreateRequest) throws NoSuchAlgorithmException;

    /**
     * 결제를 승인하고 비즈니스 로직을 실행합니다.
     * <p>
     * 클라이언트로부터 받은 영수증 정보를 Bootpay 서버와 검증한 후,
     * 금액 일치 여부를 확인하고 결제를 완료 처리합니다.
     * </p>
     *
     * @param orderId 주문 ID
     * @param receiptData 클라이언트로부터 받은 영수증 JSON 데이터
     * @throws Exception Bootpay API 호출 실패, 금액 불일치 등의 오류 시
     */
    void purchaseItem(String orderId, String receiptData) throws Exception;

    /**
     * 결제를 취소합니다.
     * <p>
     * Bootpay를 통해 결제 취소를 요청하고, 취소 결과를 반환합니다.
     * </p>
     *
     * @param orderId 결제 취소 주문 아이디
     * @param paymentCancelRequest 결제 취소 요청 정보
     * @return 취소된 결제의 영수증 정보
     * @throws Exception Bootpay API 호출 실패 시
     */
    BootpayReceiptDto cancelPayment(String orderId, PaymentCancelRequest paymentCancelRequest) throws Exception;

    /**
     * 결제를 무효(취소)합니다.
     * <p>
     * Bootpay를 통해 결제 취소를 요청하고, 취소 결과를 반환합니다.
     * 이 API는 사용자가 결제를 완료했지만 서버오류로 인해서 검증이 안되었을때 클라이언트에서 호출합니다.
     * </p>
     *
     * @return 취소된 결제의 영수증 정보
     * @throws Exception Bootpay API 호출 실패 시
     */
    BootpayReceiptDto voidPayment(String receiptId) throws Exception;

    /**
     * Bootpay로부터 영수증 정보를 조회합니다.
     *
     * @param receiptId 조회할 영수증 ID
     * @return 조회된 영수증 정보
     * @throws Exception Bootpay API 호출 실패 시
     */
    BootpayReceiptDto getPreReceipt(String receiptId) throws Exception;

    /**
     * Bootpay Webhook 요청을 처리합니다.
     * <p>
     * Bootpay로부터 전송된 Webhook 데이터를 파싱하여
     * 이벤트 타입에 맞는 처리를 수행합니다.
     * </p>
     *
     * @param webhookData Bootpay로부터 받은 Webhook JSON 데이터
     * @throws Exception 데이터 파싱 또는 처리 중 오류 시
     */
    void onBootpayWebhook(String webhookData) throws Exception;

    /**
     * 주문 ID로 결제 정보를 조회합니다.
     *
     * @param orderId 조회할 주문 ID
     * @return 조회된 Payment 엔티티
     * @throws com.eneifour.fantry.payment.exception.NotFoundPaymentException 결제 정보가 없을 시
     */
    Payment verifyPayment(String orderId);
}
