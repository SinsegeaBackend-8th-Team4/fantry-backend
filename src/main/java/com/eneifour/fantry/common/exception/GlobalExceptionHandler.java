package com.eneifour.fantry.common.exception;

import com.eneifour.fantry.account.exception.AccountException;
import com.eneifour.fantry.address.exception.AddressException;
import com.eneifour.fantry.auction.exception.BusinessException;
import com.eneifour.fantry.auction.exception.ErrorCode;
import com.eneifour.fantry.common.util.file.FileException;
import com.eneifour.fantry.member.exception.MemberException;
import com.eneifour.fantry.report.exception.ReportException;
import com.eneifour.fantry.security.exception.AuthException;
import com.eneifour.fantry.cs.exception.CsException;
import com.eneifour.fantry.payment.exception.*;
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
        log.error("Auction exception occurred [ Message: {}]", errorCode.getMessage());
        return new ResponseEntity<>(errorCode, ex.getErrorCode().getStatus());
    }

    @ExceptionHandler(ReportException.class)
    public ResponseEntity<ErrorResponse> handleFileException(ReportException ex) {
        log.error("신고에서 예외 발생: {}",  ex.getReportErrorCode().getMessage());
        ErrorResponse response = ErrorResponse.of(
                ex.getReportErrorCode().getStatus(),
                ex.getReportErrorCode().getCode(),
                ex.getReportErrorCode().getMessage()
        );
        return new ResponseEntity<>(response, ex.getReportErrorCode().getStatus());
    }

    @ExceptionHandler(AddressException.class)
    public ResponseEntity<ErrorResponse> handleFileException(AddressException ex) {
        log.error("배송지에서 예외 발생: {}",   ex.getAddressErrorCode().getMessage());
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

    @ExceptionHandler(CsException.class)
    public ResponseEntity<ErrorResponse> handleCsException(CsException ex) {
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
        log.error("BootpayException 발생: {}", ex.getErrorCode().getMessage(), ex);
        ErrorResponse response = ErrorResponse.of(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getCode(),
                ex.getErrorCode().getMessage()
        );
        return new ResponseEntity<>(response, ex.getErrorCode().getStatus());
    }

    @ExceptionHandler({
            ConcurrentPaymentException.class,
            NotFoundPaymentException.class,
            PaymentAmountMismatchException.class,
            PaymentCancelException.class,
            PaymentConfirmException.class,
            ProductNotAvailableForSaleException.class,
            TokenIssuedFailException.class,
            CancellableAmountExceededException.class,
            CreatePaymentFailedException.class,
            GhostPaymentCleanupFailedException.class
    })
    public ResponseEntity<ErrorResponse> handlePaymentException(RuntimeException ex) {
        PaymentErrorCode errorCode = null;

        if (ex instanceof ConcurrentPaymentException) {
            errorCode = ((ConcurrentPaymentException) ex).getErrorCode();
        } else if (ex instanceof NotFoundPaymentException) {
            errorCode = ((NotFoundPaymentException) ex).getErrorCode();
        } else if (ex instanceof PaymentAmountMismatchException) {
            errorCode = ((PaymentAmountMismatchException) ex).getErrorCode();
        } else if (ex instanceof PaymentCancelException) {
            errorCode = ((PaymentCancelException) ex).getErrorCode();
        } else if (ex instanceof PaymentConfirmException) {
            errorCode = ((PaymentConfirmException) ex).getErrorCode();
        } else if (ex instanceof ProductNotAvailableForSaleException) {
            errorCode = ((ProductNotAvailableForSaleException) ex).getErrorCode();
        } else if (ex instanceof TokenIssuedFailException) {
            errorCode = ((TokenIssuedFailException) ex).getErrorCode();
        } else if (ex instanceof CancellableAmountExceededException) {
            errorCode = ((CancellableAmountExceededException) ex).getErrorCode();
        } else if (ex instanceof CreatePaymentFailedException) {
            errorCode = ((CreatePaymentFailedException) ex).getErrorCode();
        } else if (ex instanceof GhostPaymentCleanupFailedException) {
            errorCode = ((GhostPaymentCleanupFailedException) ex).getErrorCode();
        }

        log.error("PaymentException 발생: {}", errorCode != null ? errorCode.getMessage() : ex.getMessage(), ex);

        if (errorCode != null) {
            ErrorResponse response = ErrorResponse.of(
                    errorCode.getStatus(),
                    errorCode.getCode(),
                    errorCode.getMessage()
            );
            return new ResponseEntity<>(response, errorCode.getStatus());
        }

        throw ex;
    }
}
