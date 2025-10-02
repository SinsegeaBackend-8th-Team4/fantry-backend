package com.eneifour.fantry.common.exception;

import com.eneifour.fantry.auction.exception.BusinessException;
import com.eneifour.fantry.auction.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalWebSocketExceptionHandler {

    @MessageExceptionHandler(BusinessException.class)
    @SendToUser("/queue/errors")
    public Map<String, Object> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn("Business exception occurred for a user: [Code: {}, Message: {}]", errorCode.getCode(), errorCode.getMessage());
        return createErrorResponse(errorCode);
    }

    @MessageExceptionHandler(Exception.class)
    @SendToUser("/queue/errors")
    public Map<String, Object> handleUnhandledException(Exception ex) {
        log.error("Unhandled exception occurred in WebSocket layer", ex);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return createErrorResponse(errorCode);
    }

    private Map<String, Object> createErrorResponse(ErrorCode errorCode) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", errorCode.getStatus().value());
        errorResponse.put("error", errorCode.getStatus().getReasonPhrase());
        errorResponse.put("code", errorCode.getCode());
        errorResponse.put("message", errorCode.getMessage());
        return errorResponse;
    }
}
