package com.eneifour.fantry.common.exception;

import com.eneifour.fantry.account.exception.AccountException;
import com.eneifour.fantry.address.exception.AddressException;
import com.eneifour.fantry.auction.exception.BusinessException;
import com.eneifour.fantry.auction.exception.ErrorCode;
import com.eneifour.fantry.common.util.file.FileException;
import com.eneifour.fantry.inquiry.exception.InquiryException;
import com.eneifour.fantry.member.exception.MemberException;
import com.eneifour.fantry.notification.exception.NotificationException;
import com.eneifour.fantry.payment.exception.BootpayException;
import com.eneifour.fantry.payment.exception.PaymentException;
import com.eneifour.fantry.report.exception.ReportException;
import com.eneifour.fantry.security.exception.AuthException;
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

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ErrorResponse> handleFileException(MemberException e) {
        log.error("회원에서 예외 발생: {}", e.getMemberCode().getMessage());
        ErrorResponse response = ErrorResponse.of(
                e.getMemberCode().getStatus(),
                e.getMemberCode().getCode(),
                e.getMemberCode().getMessage()
        );
        return new ResponseEntity<>(response, e.getMemberCode().getStatus());
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleFileException(AuthException e) {
        log.error("인증에서 예외 발생: {}", e.getAuthErrorCode().getMessage());
        ErrorResponse response = ErrorResponse.of(
                e.getAuthErrorCode().getStatus(),
                e.getAuthErrorCode().getCode(),
                e.getAuthErrorCode().getMessage()
        );
        return new ResponseEntity<>(response, e.getAuthErrorCode().getStatus());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorCode> handleAuctionException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.error("Auction / Bid / Order 예외 발생 [ Message: {}]", errorCode.getMessage());
        return new ResponseEntity<>(errorCode, ex.getErrorCode().getStatus());
    }

    @ExceptionHandler(ReportException.class)
    public ResponseEntity<ErrorResponse> handleFileException(ReportException ex) {
        log.error("신고에서 예외 발생: {}", ex.getReportErrorCode().getMessage());
        ErrorResponse response = ErrorResponse.of(
                ex.getReportErrorCode().getStatus(),
                ex.getReportErrorCode().getCode(),
                ex.getReportErrorCode().getMessage()
        );
        return new ResponseEntity<>(response, ex.getReportErrorCode().getStatus());
    }

    @ExceptionHandler(AddressException.class)
    public ResponseEntity<ErrorResponse> handleFileException(AddressException ex) {
        log.error("배송지에서 예외 발생: {}", ex.getAddressErrorCode().getMessage());
        ErrorResponse response = ErrorResponse.of(
                ex.getAddressErrorCode().getStatus(),
                ex.getAddressErrorCode().getCode(),
                ex.getAddressErrorCode().getMessage()
        );
        return new ResponseEntity<>(response, ex.getAddressErrorCode().getStatus());
    }

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ErrorResponse> handleFileException(AccountException ex) {
        log.error("계좌에서 예외 발생: {}", ex.getAccountErrorCode().getMessage());
        ErrorResponse response = ErrorResponse.of(
                ex.getAccountErrorCode().getStatus(),
                ex.getAccountErrorCode().getCode(),
                ex.getAccountErrorCode().getMessage()
        );
        return new ResponseEntity<>(response, ex.getAccountErrorCode().getStatus());
    }

    @ExceptionHandler(InquiryException.class)
    public ResponseEntity<ErrorResponse> handleCsException(InquiryException ex) {
        log.error("CsException 발생: {}", ex.getErrorCode().getMessage());
        ErrorResponse response = ErrorResponse.of(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getCode(),
                ex.getErrorCode().getMessage()
        );
        return new ResponseEntity<>(response, ex.getErrorCode().getStatus());
    }

    @ExceptionHandler(BootpayException.class)
    public ResponseEntity<ErrorResponse> handleBootpayException(BootpayException ex) {
        log.error("BootpayException 발생: {}", ex.getMessage(), ex);
        ErrorResponse response = ErrorResponse.of(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getCode(),
                ex.getErrorCode().getMessage()
        );
        return new ResponseEntity<>(response, ex.getErrorCode().getStatus());
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorResponse> handlePaymentException(PaymentException ex) {
        log.error("PaymentException 발생: {}", ex.getMessage(), ex);
        ErrorResponse response = ErrorResponse.of(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getCode(),
                ex.getErrorCode().getMessage()
        );
        return new ResponseEntity<>(response, ex.getErrorCode().getStatus());
    }

    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<ErrorResponse> handleNotificationException(NotificationException ex) {
        log.error("NotificationException 발생: {}", ex.getErrorCode().getMessage());
        ErrorResponse response = ErrorResponse.of(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getCode(),
                ex.getErrorCode().getMessage()
        );
        return new ResponseEntity<>(response, ex.getErrorCode().getStatus());
    }
}
