package com.eneifour.fantry.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/***
 * 기본적인 접근 권한 설정.
 * 로그인 개발전까진 /api 경로로 로그인 없이 테스트 가능
 * /actuator/health, info 등
 * @author 재환
 */
@Configuration
@EnableWebSecurity // 스프링 시큐리티의 웹 보안 설정을 활성화합니다.
public class SecurityConfig {

    @Bean // 이 메서드가 반환하는 객체를 스프링의 Bean으로 등록합니다.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // 🔽 여기에 로그인 없이 접근을 허용할 URL 경로 목록을 작성합니다.
                        .requestMatchers(
                                "/actuator/**",
                                "/api/**"
                        ).permitAll() // 위에 명시된 경로들은 모두 허용

                        // 🔽 위에서 허용한 경로 외의 나머지 모든 요청은 반드시 인증(로그인)을 거쳐야 합니다.
                        .anyRequest().authenticated()
                )
                // 🔽 기본 설정으로 폼 로그인 방식을 사용합니다. (로그인 페이지 자동 생성)
                .formLogin(withDefaults());

        return http.build();
    }
}