package com.eneifour.fantry.settlement.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 정산(Settlement) 도메인 관련 에러 코드를 정의하는 Enum.
 */
@Getter
@RequiredArgsConstructor
public enum SettlementErrorCode {

    SETTING_NOT_FOUND(HttpStatus.NOT_FOUND, "SETTLE001", "정산 설정이 존재하지 않습니다. 먼저 설정을 등록해주세요."),
    INVALID_COMMISSION_RATE(HttpStatus.BAD_REQUEST, "SETTLE002", "수수료율은 0과 100 사이의 값이어야 합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}