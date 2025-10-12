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

    // Bid Exceptions
    BID_AMOUNT_INVALID(HttpStatus.BAD_REQUEST, "B001", "입찰 금액을 올바르게 입력해주세요."),
    BID_UNIT_INVALID(HttpStatus.BAD_REQUEST, "B002", "입찰 금액은 100원 단위로 입력해야 합니다."),
    BID_TOO_LOW_START(HttpStatus.BAD_REQUEST, "B003", "첫 입찰 금액은 시작가보다 높아야 합니다."),
    BID_TOO_LOW_INCREMENT(HttpStatus.BAD_REQUEST, "B004", "입찰 금액이 현재가보다 충분히 높지 않습니다."),

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
