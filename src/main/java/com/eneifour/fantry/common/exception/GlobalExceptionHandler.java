package com.eneifour.fantry.common.exception;

import com.eneifour.fantry.auction.exception.BusinessException;
import com.eneifour.fantry.auction.exception.ErrorCode;
import com.eneifour.fantry.common.util.file.FileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/***
 * 전역 예외처리 핸들러
 * @author 재환
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileException.class)
    public ResponseEntity<ErrorResponse> handleFileException(FileException ex) {
        log.error("FileException 발생: {}", ex.getErrorCode().getMessage());
        ErrorResponse response = ErrorResponse.of(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getCode(),
                ex.getErrorCode().getMessage()
        );
        return new ResponseEntity<>(response, ex.getErrorCode().getStatus());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorCode> handleAuctionException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.error("Auction exception occurred [ Message: {}]",  errorCode.getMessage());
        return new ResponseEntity<>(errorCode, ex.getErrorCode().getStatus());
    }
}
