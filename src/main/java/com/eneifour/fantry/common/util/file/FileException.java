package com.eneifour.fantry.common.util.file;

import lombok.Getter;

/***
 * 파일 예외처리
 * @author 재환
 */
@Getter
public class FileException extends RuntimeException {

    private final FileErrorCode errorCode;

    public FileException(FileErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public FileException(FileErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

}
