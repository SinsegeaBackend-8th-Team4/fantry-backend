package com.eneifour.fantry.settlement.excpetion;

import lombok.Getter;

/**
 * 정산(Settlement) 도메인에서 발생하는 커스텀 예외 클래스.
 */
@Getter
public class SettlementException extends RuntimeException {

    private final SettlementErrorCode errorCode;

    public SettlementException(SettlementErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public SettlementException(SettlementErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
