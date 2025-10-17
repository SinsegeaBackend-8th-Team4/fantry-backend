package com.eneifour.fantry.refund.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 환불/반품 도메인에서 발생할 수 있는 특정한 예외 상황들을 정의한 Enum입니다.
 * <p>각 코드는 HTTP 상태, 고유 코드, 그리고 클라이언트에게 전달될 메시지를 포함합니다.
 */
@Getter
@RequiredArgsConstructor
public enum ReturnErrorCode {
    RETURN_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "RET001", "해당 환불 요청을 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "RET002", "환불할 주문 정보를 찾을 수 없습니다."),
    STATUS_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "RET003", "지원하지 않는 상태 값입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "RET004", "해당 기능에 대한 접근 권한이 없습니다."),
    DUPLICATE_REQUEST(HttpStatus.CONFLICT, "RET005", "이미 해당 주문에 대한 환불 요청이 존재합니다."),
    PAYMENT_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "RET006", "환불에 필요한 결제 정보를 찾을 수 없습니다."),
    REFUND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "RET007", "외부 결제 API를 통한 환불 처리에 실패했습니다."),
    BUYER_NOT_FOUND(HttpStatus.NOT_FOUND, "RET008", "환불 요청 대상 사용자를 찾을 수 없습니다."),
    CANCELLATION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "RET009", "이미 처리 중인 환불 요청은 철회할 수 없습니다."),
    NOT_REFUNDABLE_STATUS(HttpStatus.BAD_REQUEST, "RET010", "배송중인 상품은 반품이 불가능 합니다."),
    REFUND_PERIOD_EXPIRED(HttpStatus.BAD_REQUEST,"RET011" , "반품/환불은 배송완료일로부터 일주일 이내에만 가능합니다." );


    private final HttpStatus status;
    private final String code;
    private final String message;
}
