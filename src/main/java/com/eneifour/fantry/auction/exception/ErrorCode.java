package com.eneifour.fantry.auction.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Auction Exceptions
    AUCTION_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "존재하지 않는 경매 상품입니다."),
    AUCTION_NOT_ACTIVE(HttpStatus.BAD_REQUEST, "A002", "현재 진행 중인 경매가 아닙니다."),
    AUCTION_NOT_MATCH_INSPECTION(HttpStatus.BAD_REQUEST, "A003", "상품 정보가 일치하지 않습니다"),
    CANNOT_RESCHEDULE_ACTIVE_AUCTION(HttpStatus.BAD_REQUEST,"A004","현재 판매중인 상품은 시작시간과 마감시간을 변경할 수 없습니다"),
    INVALID_AUCTION_TIME(HttpStatus.BAD_REQUEST,"A005","판매 시작 시간과 마감시간이 올바르지 않습니다"),
    CANNOT_DELETE_ACTIVE_AUCTION(HttpStatus.BAD_REQUEST,"A006","현재 판매중인 상품은 삭제할 수 없습니다"),

    // Bid Exceptions
    BID_AMOUNT_INVALID(HttpStatus.BAD_REQUEST, "B001", "입찰 금액을 올바르게 입력해주세요."),
    BID_UNIT_INVALID(HttpStatus.BAD_REQUEST, "B002", "입찰 금액은 100원 단위로 입력해야 합니다."),
    BID_TOO_LOW_START(HttpStatus.BAD_REQUEST, "B003", "첫 입찰 금액은 시작가보다 높아야 합니다."),
    BID_TOO_LOW_INCREMENT(HttpStatus.BAD_REQUEST, "B004", "입찰 금액이 현재가보다 충분히 높지 않습니다."),

    // Order Exceptions
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "O001", "존재하지 않는 주문입니다."),
    ORDER_INVALID_STATUS(HttpStatus.BAD_REQUEST, "O002", "유효하지 않은 주문 상태입니다."),
    ORDER_PAYMENT_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "O003", "이미 결제가 완료된 주문입니다."),
    ORDER_PAYMENT_NOT_PENDING(HttpStatus.BAD_REQUEST, "O004", "결제 대기 상태에서만 결제 완료로 변경할 수 있습니다."),
    ORDER_SHIPMENT_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "O005", "결제가 완료된 주문만 배송 준비가 가능합니다."),
    ORDER_ALREADY_SHIPPED(HttpStatus.BAD_REQUEST, "O006", "이미 배송이 시작된 주문입니다."),
    ORDER_DELIVERY_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "O007", "배송 중 상태에서만 배송 완료로 변경할 수 있습니다."),
    ORDER_CONFIRMATION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "O008", "배송 완료 상태에서만 구매 확정할 수 있습니다."),
    ORDER_CANCELLATION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "O009", "해당 주문은 취소할 수 없습니다."),
    ORDER_REFUND_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "O010", "배송 완료 또는 구매 확정 상태에서만 환불 요청이 가능합니다."),
    ORDER_ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "O011", "이미 취소된 주문입니다."),
    ORDER_ALREADY_REFUNDED(HttpStatus.BAD_REQUEST, "O012", "이미 환불된 주문입니다."),
    ORDER_INVALID_TRANSITION(HttpStatus.BAD_REQUEST, "O013", "잘못된 주문 상태 전이 요청입니다."),

    // Member Exceptions
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "존재하지 않는 회원입니다."),

    // Redis Exceptions
    REDIS_SERVER_EXCEPTION(HttpStatus.NOT_FOUND, "R001", "레디스 서버 응답이 없습니다."),

    // Unhandled Exceptions
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");



    private final HttpStatus status;
    private final String code;
    private final String message;
}
