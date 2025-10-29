package com.eneifour.fantry.common.config;

import com.eneifour.fantry.payment.domain.config.BootpayProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableConfigurationProperties(BootpayProperties.class)
public class PaymentConfig {
    private final BootpayProperties bootpayProperties;

    public PaymentConfig(BootpayProperties bootpayProperties) {
        this.bootpayProperties = bootpayProperties;
    }

    @Bean
    public WebClient bootpayWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .responseTimeout(Duration.ofMillis(bootpayProperties.getTimeoutResponseMillis()))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5, TimeUnit.SECONDS))
                );

        return WebClient.builder()
                .baseUrl(bootpayProperties.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
