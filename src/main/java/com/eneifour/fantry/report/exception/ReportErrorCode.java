package com.eneifour.fantry.report.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReportErrorCode {
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "존재하지 않는 신고내역 입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
