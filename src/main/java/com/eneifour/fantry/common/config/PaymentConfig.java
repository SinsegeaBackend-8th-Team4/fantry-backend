package com.eneifour.fantry.common.config;

import com.eneifour.fantry.payment.exception.TokenIssuedFailException;
import kr.co.bootpay.pg.Bootpay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;

@Slf4j
@Configuration
public class PaymentConfig {

    @Value("${config.bootpay.appid}")
    String applicationId;
    @Value("${config.bootpay.secretKey}")
    String secretKey;

    @Bean
    public Bootpay bootpay() {
        return new Bootpay(applicationId, secretKey);
    }

    @Bean
    @Scope("prototype")
    @Qualifier("bootpayToken")
    public String getToken(Bootpay bootpay) throws Exception {
        HashMap<String, Object> result = bootpay.getAccessToken();
        if (result.get("error_code") == null) {
            // 토큰 발급 성공
            return bootpay.token;
        } else {
            // 토큰 발급 실패
            log.warn("goGetToken fail: {}", result);
            throw new TokenIssuedFailException("goGetToken fail");
        }
    }
}
