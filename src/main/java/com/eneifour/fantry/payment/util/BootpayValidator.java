package com.eneifour.fantry.payment.util;

import com.eneifour.fantry.payment.domain.bootpay.BootpayError;
import com.eneifour.fantry.payment.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class BootpayValidator {
    public void checkBootpayReceipt(Map<String, Object> result) throws NotFoundReceiptException {
        log.info("checkBootpayReceipt: {}", result);
        if (result.get("http_status") == null || HttpStatus.OK.value() == (int) result.get("http_status")) {
            return;
        }
        Object errorCode = result.get("error_code");
        if (errorCode != null) {
            String message = (String) result.get("message");
            BootpayError error = BootpayError.fromCode((String) errorCode);
            switch (error) {
                case TOKEN_KEY_INVALID -> throw new TokenInvalidException();
                case APP_KEY_NOT_FOUND -> throw new AppKeyNotFoundException();
                case APP_KEY_NOT_REST -> throw new AppKeyNotRESTException();
                case APP_SK_NOT_MATCHED -> throw new AppSecretKeyNotMatchedException();
                case RC_NOT_FOUND -> throw new NotFoundReceiptException(message);
                case RC_CANCEL_SERVER_ERROR -> throw new ConfirmPaymentCancelFailedException(message);
                case RC_CANCEL_CRITICAL_ERROR -> throw new ConfirmPaymentCancelCriticalException(message);
                case RC_CONFIRM_FAILED -> throw new ConfirmPaymentApproveFailedException(message);
                case RC_CONFIRM_CRITICAL_FAILED -> throw new ConfirmPaymentApproveCriticalException(message);
            }
        }
    }
}
