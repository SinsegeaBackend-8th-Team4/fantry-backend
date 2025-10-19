package com.eneifour.fantry.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //구독 경로 prefix 설정
        registry.enableSimpleBroker("/topic");
        //클라이언트 -> 서버 메시지 경로 prefix 설정
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 기존 프론트엔드 앱을 위한 SockJS 엔드포인트 (그대로 둠)
        registry.addEndpoint("/api/ws-auction")
                .setAllowedOrigins("http://localhost:5173")
                .withSockJS();

        // K6 부하 테스트를 위한 순수 웹소켓 엔드포인트 추가
        registry.addEndpoint("/ws-load-test")
                .setAllowedOrigins("*");

    }
}
