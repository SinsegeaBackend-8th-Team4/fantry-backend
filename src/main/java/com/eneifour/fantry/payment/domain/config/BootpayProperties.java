package com.eneifour.fantry.payment.domain.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "config.bootpay")
public class BootpayProperties {
    private String appId;
    private String secretKey;
    private String baseUrl;
    private int timeoutResponseMillis = 30000;
}
