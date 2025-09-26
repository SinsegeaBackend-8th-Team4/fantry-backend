package com.eneifour.fantry.security.exception.controller;

import com.eneifour.fantry.security.exception.dto.ErrorResponse;
import com.eneifour.fantry.security.exception.exception.InvalidTokenTypeException;
import com.eneifour.fantry.security.exception.exception.TimeoutAccessTokenException;
import com.eneifour.fantry.security.exception.exception.TokenHeaderVerificationException;
import com.eneifour.fantry.security.exception.exception.UnauthorizedException;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//시큐리티의 모든 예외를 처리하기 위한 전역 예외 처리
@ControllerAdvice
public class SecurityExceptionAdvice {
    //잘못된 헤더 요청
    @ExceptionHandler(TokenHeaderVerificationException.class)
    public ResponseEntity<ErrorResponse> handleTokenHeaderVerificationException(TokenHeaderVerificationException e){
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // AccessToken 만료 인증 오류
    @ExceptionHandler(TimeoutAccessTokenException.class)
    public ResponseEntity<ErrorResponse> handleTokenAccessTokenException(TimeoutAccessTokenException e){
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    //잘못된 토큰 유형 오류
    @ExceptionHandler(InvalidTokenTypeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenTypeException(InvalidTokenTypeException e){
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    //잘못된 인증 요청
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e){
        ErrorResponse errorResponse = new ErrorResponse(
          HttpStatus.UNAUTHORIZED.value(),
          HttpStatus.UNAUTHORIZED.getReasonPhrase(),
          e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }


    //그 밖에 모든 예외 처리 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex){
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "예상치 못한 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
