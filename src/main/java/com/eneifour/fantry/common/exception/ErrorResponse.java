package com.eneifour.fantry.common.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/***
 * 공통 에러(예외) 형식
 * @author 재환
 */
@Getter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public static ErrorResponse of(HttpStatus httpStatus, String code, String message) {
        return ErrorResponse.builder()
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .code(code)
                .message(message)
                .build();
    }
}