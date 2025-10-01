package com.eneifour.fantry.payment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Payment 서비스 에러 코드
 */
@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode {
    // Bootpay API 관련 에러 (401)
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "PAY001", "Access Token이 잘못되었습니다."),
    APP_KEY_NOT_FOUND(HttpStatus.UNAUTHORIZED, "PAY002", "REST API Application ID가 잘못된 값입니다."),
    APP_KEY_NOT_REST(HttpStatus.UNAUTHORIZED, "PAY003", "REST API Application ID가 아닙니다."),
    APP_SK_NOT_MATCHED(HttpStatus.UNAUTHORIZED, "PAY004", "Private Key가 잘못되었습니다."),

    // Bootpay Receipt 관련 에러 (404)
    RC_NOT_FOUND(HttpStatus.NOT_FOUND, "PAY005", "영수증을 찾을 수 없거나 조회 권한이 없습니다."),

    // Bootpay 취소 관련 에러 (400/500)
    RC_CANCEL_SERVER_ERROR(HttpStatus.BAD_REQUEST, "PAY006", "PG사에서 결제 취소를 실패했습니다."),
    RC_CANCEL_CRITICAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "PAY007", "결제 취소 요청 중 치명적인 에러가 발생했습니다."),

    // Bootpay 승인 관련 에러 (400/500)
    RC_CONFIRM_FAILED(HttpStatus.BAD_REQUEST, "PAY008", "PG사에서 결제 승인에 실패했습니다."),
    RC_CONFIRM_CRITICAL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PAY009", "결제 승인 요청 중 치명적인 에러가 발생했습니다."),

    // Bootpay 연결 및 타임아웃 에러 (500/504)
    BOOTPAY_CONNECTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "PAY010", "Bootpay 서버와 연결에 실패했습니다."),
    BOOTPAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "PAY011", "Bootpay 서버 응답 시간이 초과되었습니다."),
    BOOTPAY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "PAY012", "Bootpay 처리 중 에러가 발생했습니다."),

    // Payment 도메인 에러 (404)
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "PAY013", "결제 정보를 찾을 수 없습니다."),

    // Payment 검증 에러 (400)
    PAYMENT_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, "PAY014", "결제 금액이 일치하지 않습니다."),
    CANCELLABLE_AMOUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "PAY015", "취소 가능 금액을 초과했습니다."),

    // Payment 처리 에러 (500)
    PAYMENT_CANCEL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PAY016", "결제 취소에 실패했습니다."),
    PAYMENT_CONFIRM_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PAY017", "결제 승인에 실패했습니다."),
    CREATE_PAYMENT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PAY018", "결제 생성에 실패했습니다."),
    GHOST_PAYMENT_CLEANUP_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PAY019", "유령 결제 정리에 실패했습니다."),

    // 동시성 에러 (409)
    CONCURRENT_PAYMENT(HttpStatus.CONFLICT, "PAY020", "동시에 진행 중인 결제가 있습니다."),

    // 상품 관련 에러 (400)
    PRODUCT_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "PAY021", "판매 불가능한 상품입니다."),

    // 토큰 발급 에러 (500)
    TOKEN_ISSUED_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PAY022", "토큰 발급에 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
