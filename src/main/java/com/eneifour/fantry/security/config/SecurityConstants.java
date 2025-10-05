package com.eneifour.fantry.security.config;

//로그인이나 토큰 유효성 검사 없이 접근을 허용할 공개 URL 목록
public class SecurityConstants {

    public static final String[] PUBLIC_URIS = {
            "/actuator/**",
            "/api/login",
            "/api/reissue",
            "/api/user/**",
            "/api/type**",
            "/api/send/**",
            "/app/**",
            "/ws-auction/**",
            "/api/file/**",
            "/api/payment/**",
            "/webhook/**",
            "/static/**"
    };
}
