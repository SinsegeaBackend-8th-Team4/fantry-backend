package com.eneifour.fantry.report.exception;

import lombok.Getter;

@Getter
public class ReportException extends RuntimeException {
    private ReportErrorCode reportErrorCode;

    public ReportException(ReportErrorCode reportErrorCode) {
        super(reportErrorCode.getMessage());
        this.reportErrorCode = reportErrorCode;
    }

    public ReportException(ReportErrorCode reportErrorCode, Throwable cause) {
        super(reportErrorCode.getMessage(), cause);
        this.reportErrorCode = reportErrorCode;
    }
}
