package com.eneifour.fantry.inspection.support.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum InspectionErrorCode implements BaseErrorCode {
    INSPECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "INS001", "존재하지 않는 검수 신청 건입니다."),
    EMPTY_FILE_ATTACHED(HttpStatus.BAD_REQUEST, "INS002", "업로드된 파일이 비어있습니다."),
    INVALID_INSPECTION_STATUS(HttpStatus.BAD_REQUEST, "INS003", "유효하지 않은 검수 상태입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "INS004", "해당 검수 정보에 접근할 권한이 없습니다.");
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
