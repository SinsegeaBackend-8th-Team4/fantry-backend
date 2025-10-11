package com.eneifour.fantry.security.config;

//로그인이나 토큰 유효성 검사 없이 접근을 허용할 공개 URL 목록
public class SecurityConstants {

    public static final String[] PUBLIC_URIS = {
            "/actuator/**",
            "/api/login",
            "/api/reissue",
            "/api/user/**",
            "/api/send/**",
            "/app/**",
            "/api/auctions/**",
            "/api/bids/**",
            "/ws-auction/**",
            "/api/file/**",
            "/api/payments/**",
            "/webhook/**",
            "/static/**",
            // Swagger 관련 경로
            "/api-docs/**",         // 사용자가 접속하는 단축 주소 (리다이렉트 시작점)
            "/swagger-ui/**",       // 리다이렉트된 후 실제 UI 리소스(html, css, js)가 있는 주소
            "/v3/api-docs/**",      // API 명세(JSON)를 불러오는 주소
            "/swagger-resources/**"
    };
}
