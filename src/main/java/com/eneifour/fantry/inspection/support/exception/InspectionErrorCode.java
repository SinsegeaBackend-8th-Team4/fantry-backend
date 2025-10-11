package com.eneifour.fantry.inspection.support.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum InspectionErrorCode implements BaseErrorCode {
    // =================================================================
    // == 기본/공통 오류 (INS001 ~ INS009)
    // =================================================================
    INSPECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "INS001", "존재하지 않는 검수 신청 건입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "INS002", "해당 검수 정보에 접근할 권한이 없습니다."),
    EMPTY_FILE_ATTACHED(HttpStatus.BAD_REQUEST, "INS003", "필수 첨부 파일이 누락되었습니다."),
    INVALID_REQUEST_DATA(HttpStatus.BAD_REQUEST, "INS004", "요청 데이터가 올바르지 않습니다."), // DTO 유효성 검사 등

    // =================================================================
    // == 상태 검증 오류 (INS010 ~ INS019)
    // =================================================================
    // --- 1차 검수 관련 ---
    NOT_SUBMITTED_STATUS_FOR_FIRST_INSPECTION(HttpStatus.BAD_REQUEST, "INS010", "1차 검수는 '제출(SUBMITTED)' 상태에서만 처리할 수 있습니다."),
    ALREADY_PROCESSED_FIRST_INSPECTION(HttpStatus.BAD_REQUEST, "INS011", "이미 1차 검수가 완료된 신청 건입니다."),

    // --- 2차 검수 관련 ---
    NOT_FIRST_REVIEWED_STATUS_FOR_SECOND_INSPECTION(HttpStatus.BAD_REQUEST, "INS012", "2차 검수는 '1차 검수 완료(FIRST_REVIEWED)' 상태에서만 처리할 수 있습니다."),
    ALREADY_PROCESSED_SECOND_INSPECTION(HttpStatus.BAD_REQUEST, "INS013", "이미 2차 검수가 완료된 신청 건입니다."),

    // =================================================================
    // == 시스템/데이터 처리 오류 (INS020 ~ )
    // =================================================================
    JSON_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INS020", "검수 데이터(JSON) 처리 중 오류가 발생했습니다."),
    INSPECTION_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "INS021", "검수 정보 업데이트 중 서버 오류가 발생했습니다.")

    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
