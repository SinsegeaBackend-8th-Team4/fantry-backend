package com.eneifour.fantry.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration 
public class WebConfig implements WebMvcConfigurer {
    private final CorsProperties corsProperties;

    public WebConfig(CorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * CORS 설정
     * 추후 수정 필요
     * @Author: 재환
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // "/**"는 모든 API 엔드포인트에 CORS 설정을 적용하겠다는 의미
                
                // 쉼표로 구분된 문자열을 배열로 만든다.
                .allowedOrigins(corsProperties.getAllowedOrigins().split(","))

                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")

                // 쿠키나 인증 헤더와 같은 자격 증명(Credentials)을 허용할지 여부.
                // 프론트엔드와 세션/토큰을 주고받으려면 true로 설정해야 합니다.
                .allowCredentials(true)

                // 브라우저에서 preflight 요청 결과를 캐시할 시간을 초 단위로 지정합니다.
                .maxAge(3600);
    }

    /**
     * 파일 업로드 경로 → /static/** URL 로 접근 가능하도록 매핑
     * @Author: 혜원
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}
