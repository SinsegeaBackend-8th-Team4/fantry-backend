package com.eneifour.fantry.report.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReportErrorCode {
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "RP001", "존재하지 않는 신고내역 입니다."),
    REPORT_NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "RP002", "신고를 추가/변경하기 위한 회원이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
