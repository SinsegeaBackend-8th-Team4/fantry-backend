package com.eneifour.fantry.payment.service;

import com.eneifour.fantry.payment.domain.bootpay.BankDataDto;
import com.eneifour.fantry.payment.domain.bootpay.BootpayReceiptDto;
import com.eneifour.fantry.payment.domain.config.BootpayProperties;
import com.eneifour.fantry.payment.exception.BootpayException;
import com.eneifour.fantry.payment.domain.PaymentErrorCode;
import com.eneifour.fantry.payment.util.BootpayValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

/**
 * Bootpay 외부 결제 API와 통신하는 서비스 클래스입니다.
 * <p>
 * 이 서비스는 Bootpay의 REST API를 WebClient를 통해 호출하며,
 * 토큰 발급, 결제 조회, 결제 승인, 결제 취소 등의 기능을 제공합니다.
 * </p>
 *
 * @see BootpayReceiptDto
 * @see BootpayValidator
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BootpayService {
    private final BootpayValidator bootpayValidator;
    private final ObjectMapper objectMapper;
    private final WebClient bootpayWebClient;
    private final BootpayProperties bootpayProperties;

    /**
     * WebClient를 사용하여 Bootpay API 접근 토큰을 발급받습니다.
     *
     * @return 발급된 Access Token 문자열
     */
    private String getAccessTokenViaWebClient() {
        try {
            Map<String, Object> request = Map.of(
                    "application_id", bootpayProperties.getAppId(),
                    "private_key", bootpayProperties.getSecretKey()
            );

            Map<String, Object> response = bootpayWebClient.post()
                    .uri("/request/token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    })
                    .block();

            if (response != null && response.get("access_token") != null) {
                return (String) response.get("access_token");
            }
            throw new BootpayException(PaymentErrorCode.BOOTPAY_ERROR);
        } catch (WebClientResponseException e) {
            log.error("Bootpay API 오류", e);
            throw handleWebClientResponseException(e);
        } catch (Exception e) {
            log.error("Access Token 발급 실패", e);
            throw new BootpayException(PaymentErrorCode.TOKEN_ISSUED_FAILED, e);
        }
    }

    /**
     * WebClient를 사용하여 Bootpay 영수증 정보를 조회합니다.
     *
     * @param receiptId 조회할 영수증의 고유 ID
     * @return 조회된 BootpayReceiptDto 객체
     */
    public BootpayReceiptDto getReceiptViaWebClient(String receiptId) {
        log.info("WebClient로 Bootpay 영수증 조회 시작. receiptId: {}", receiptId);

        try {
            String token = getAccessTokenViaWebClient();
            log.info("토큰 : {}", token);
            Map<String, Object> response = bootpayWebClient.get()
                    .uri("/receipt/" + receiptId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    })
                    .block();

            BootpayReceiptDto dto = objectMapper.convertValue(response, BootpayReceiptDto.class);
            log.info("WebClient 영수증 조회 성공. receiptId: {}", receiptId);
            return dto;

        } catch (WebClientResponseException e) {
            log.error("Bootpay API 오류. receiptId: {}, status: {}", receiptId, e.getStatusCode(), e);
            throw handleWebClientResponseException(e);
        } catch (Exception e) {
            log.error("영수증 조회 실패. receiptId: {}", receiptId, e);
            throw new BootpayException(PaymentErrorCode.BOOTPAY_ERROR, e);
        }
    }

    /**
     * WebClient로 Bootpay 결제 승인
     *
     * @param receiptId 승인할 영수증 ID
     * @return 승인된 결제 영수증 정보
     */
    public BootpayReceiptDto approveViaWebClient(String receiptId) {
        log.info("WebClient로 Bootpay 결제 승인 시작. receiptId: {}", receiptId);

        try {
            String token = getAccessTokenViaWebClient();

            Map<String, Object> request = Map.of("receipt_id", receiptId);

            Map<String, Object> response = bootpayWebClient.post()
                    .uri("/confirm")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    })
                    .block();
            log.info("confirm : {}", response);
            BootpayReceiptDto dto = objectMapper.convertValue(response, BootpayReceiptDto.class);
            log.info("WebClient 결제 승인 성공. receiptId: {}", receiptId);
            return dto;

        } catch (WebClientResponseException e) {
            log.error("Bootpay API 오류. receiptId: {}, status: {}, body: {}", receiptId, e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw handleWebClientResponseException(e);
        } catch (Exception e) {
            log.error("결제 승인 실패. receiptId: {}", receiptId, e);
            throw new BootpayException(PaymentErrorCode.RC_CONFIRM_FAILED, e);
        }
    }

    /**
     * WebClient로 결제 취소
     *
     * @param receiptId    취소할 영수증 ID
     * @param cancelReason 취소 사유
     * @param username     취소를 요청한 관리자 ID
     * @param orderId      주문 ID (선택 사항)
     * @param cancelPrice  취소 금액 (부분 취소 시 사용)
     * @param bankDataDto  환불 받을 계좌 정보 (가상계좌 등 환불 시 필요)
     * @return 취소된 결제 영수증 정보
     */
    public BootpayReceiptDto cancellationViaWebClient(String receiptId, String cancelReason, String username, String orderId, String cancelPrice, BankDataDto bankDataDto) {
        log.info("WebClient로 Bootpay 결제 취소 시작. receiptId: {}", receiptId);

        try {
            String token = getAccessTokenViaWebClient();
            Map<String, Object> request = Map.of("receipt_id", receiptId, "cancel_message", cancelReason, "cancel_username", username, "cancel_id", orderId, "cancel_price", cancelPrice);
            Map<String, Object> response = bootpayWebClient.post()
                    .uri("/cancel")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    })
                    .block();
            BootpayReceiptDto dto = objectMapper.convertValue(response, BootpayReceiptDto.class);
            log.info("WebClient 결제 취소 성공. receiptId: {}", receiptId);
            return dto;
        } catch (WebClientResponseException e) {
            log.error("Bootpay API 오류. receiptId: {}, status: {}", receiptId, e.getStatusCode(), e);
            throw handleWebClientResponseException(e);
        } catch (Exception e) {
            log.error("결제 취소 실패. receiptId: {}", receiptId, e);
            throw new BootpayException(PaymentErrorCode.RC_CANCEL_SERVER_ERROR, e);
        }
    }

    /**
     * WebClient 응답 예외를 Bootpay 커스텀 예외로 변환합니다.
     * <p>
     * Bootpay API 오류 응답을 파싱하여 적절한 비즈니스 예외로 변환합니다.
     * </p>
     *
     * @param e WebClient 응답 예외
     * @return 변환된 BootpayException
     */
    private BootpayException handleWebClientResponseException(WebClientResponseException e) {
        Map<String, Object> response = e.getResponseBodyAs(new ParameterizedTypeReference<>() {
        });
        return bootpayValidator.handleBootpayErrorResponse(response);
    }
}
