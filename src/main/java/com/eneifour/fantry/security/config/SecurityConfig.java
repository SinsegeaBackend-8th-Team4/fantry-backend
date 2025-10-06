package com.eneifour.fantry.security.config;

import com.eneifour.fantry.common.config.CorsProperties;
import com.eneifour.fantry.security.filter.AuthJwtTokenFilter;
import com.eneifour.fantry.security.service.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/***
 * 기본적인 접근 권한 설정.
 * 로그인 개발전까진 /api 경로로 로그인 없이 테스트 가능
 * /actuator/health, info 등
 * @author 재환
 */
@Configuration
@EnableWebSecurity // 스프링 시큐리티의 웹 보안 설정을 활성화합니다.
public class SecurityConfig {

    private final CorsProperties corsProperties;
    private final AuthJwtTokenFilter authJwtTokenFilter;

    public SecurityConfig(CorsProperties corsProperties,  AuthJwtTokenFilter authJwtTokenFilter) {
        this.corsProperties = corsProperties;
        this.authJwtTokenFilter = authJwtTokenFilter;
    }

    //암호화 설정
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //AuthenticationManager 등록
    @Bean
    public AuthenticationManager authenticationManagerBean(CustomUserDetailService userDetailService, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        //유저 얻기
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);

        //비밀번호 검증
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(provider);
    }

    @Bean // 이 메서드가 반환하는 객체를 스프링의 Bean으로 등록합니다.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //CORS 설정 - 보안 필터에서 허용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                //경로별 인가 작업
                .authorizeHttpRequests(authorize -> authorize
                        // 🔽 여기에 로그인 없이 접근을 허용할 URL 경로 목록을 작성합니다.
                        .requestMatchers(
                                SecurityConstants.PUBLIC_URIS
                        ).permitAll() // 위에 명시된 경로들은 모두 허용

                        // 여기에 관리자만 접근을 허용할 URL 경로 목록 작성
                        .requestMatchers(
                                "/api/admin/**"
                        ).hasAnyRole("SADMIN","ADMIN")  //슈퍼관리자(SADMIN), 관리자(ADMIN)

                        // 🔽 위에서 허용한 경로 외의 나머지 모든 요청은 반드시 인증(로그인)을 거쳐야 합니다.
                        .anyRequest().authenticated()
                )
                .formLogin(auth -> auth.disable())
                .httpBasic(auth -> auth.disable())
                .logout(logout -> logout.disable())

                //필터등록
                .addFilterBefore(authJwtTokenFilter, AuthenticationFilter.class)

                //세션 설정
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    // 보안 필터에서 시큐리티에서 CORS 설정 처리
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setMaxAge(3600L);
        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 URL에 적용
        return source;
    }

    /***
     * 배포 테스용 임시 user
     * @author 재환
     */
    @Bean
    public UserDetailsService userDetailsService() {
        // 'user'라는 아이디와 'password'라는 비밀번호를 가진 테스트용 계정을 생성합니다.
        // {noop}은 비밀번호를 암호화하지 않고 평문으로 사용하겠다는 의미입니다. (개발용)
        UserDetails user = User.withUsername("user")
                .password("{noop}password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}