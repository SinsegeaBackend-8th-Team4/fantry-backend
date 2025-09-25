package com.eneifour.fantry.common.util.file;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/***
 * File 서비스 에러 코드
 * @author 재환
 */
@Getter
@RequiredArgsConstructor
public enum FileErrorCode {
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "F001", "파일을 찾을 수 없습니다."),
    UPLOAD_FILE_EMPTY(HttpStatus.BAD_REQUEST, "F002", "업로드할 파일이 비어있습니다."),
    FILE_STORAGE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "F003", "파일 저장에 실패했습니다."),
    FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "F004", "파일 삭제에 실패했습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "F005", "파일에 대한 접근 권한이 없습니다."),
    FILE_RESTORE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "F006", "파일 복구에 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}