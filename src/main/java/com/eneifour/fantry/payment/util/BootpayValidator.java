package com.eneifour.fantry.payment.util;

import com.eneifour.fantry.payment.domain.bootpay.BootpayError;
import com.eneifour.fantry.payment.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class BootpayValidator {
    public BootpayException handleBootpayErrorResponse(Map<String, Object> result) throws BootpayException {
        log.info("checkBootpayReceipt: {}", result);
        String errorCode = (String) result.get("error_code");
        log.error("Bootpay API 에러 코드: {}, 메시지: {}", errorCode, result.get("message"));

        BootpayError error = BootpayError.fromCode(errorCode);
        return switch (error) {
            case TOKEN_KEY_INVALID -> new TokenInvalidException();
            case APP_KEY_NOT_FOUND -> new AppKeyNotFoundException();
            case APP_KEY_NOT_REST -> new AppKeyNotRESTException();
            case APP_SK_NOT_MATCHED -> new AppSecretKeyNotMatchedException();
            case RC_NOT_FOUND -> new NotFoundReceiptException();
            case RC_CANCEL_SERVER_ERROR -> new ConfirmPaymentCancelFailedException();
            case RC_CANCEL_CRITICAL_ERROR -> new ConfirmPaymentCancelCriticalException();
            case RC_CONFIRM_FAILED -> new ConfirmPaymentApproveFailedException();
            case RC_CONFIRM_CRITICAL_FAILED -> new ConfirmPaymentApproveCriticalException();
        };
    }
}
