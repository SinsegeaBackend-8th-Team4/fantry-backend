package com.eneifour.fantry.payment.aspect;

import com.eneifour.fantry.payment.annotation.RequireBootpayToken;
import com.eneifour.fantry.payment.exception.NotFoundReceiptException;
import com.eneifour.fantry.payment.exception.TokenIssuedFailException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
public class BootpayTokenAspect {
    private final ObjectProvider<String> tokenProvider;

    public BootpayTokenAspect(@Qualifier("bootpayToken") ObjectProvider<String> tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Around("@annotation(requireBootpayToken)")
    public Object handleTokenGeneration(
            ProceedingJoinPoint joinPoint,
            RequireBootpayToken requireBootpayToken
    ) throws Throwable {
        try {
            log.debug("Bootpay getAccessToken Call");
            tokenProvider.getObject();
            return joinPoint.proceed();
        } catch (NotFoundReceiptException e) {
            log.error("Token generation failed", e);
            throw e;
        } catch (Exception e) {
            log.error("Token generation failed", e);
            throw new TokenIssuedFailException("Failed to process request with token", e);
        }
    }
}
