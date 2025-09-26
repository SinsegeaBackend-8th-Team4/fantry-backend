package com.eneifour.fantry.security.model;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.domain.RoleType;
import com.eneifour.fantry.member.model.JpaMemberRepository;
import com.eneifour.fantry.security.dto.AccessTokenResponse;
import com.eneifour.fantry.security.dto.CustomUserDetails;
import com.eneifour.fantry.security.dto.TokenResponse;
import com.eneifour.fantry.security.exception.exception.UnauthorizedException;
import com.eneifour.fantry.security.util.CookieUtil;
import com.eneifour.fantry.security.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisTokenService redisTokenService;
    @Value("${spring.jwt.refresh-days}") long refreshDays;
    @Value("${spring.jwt.access-minutes}") long accessMinutes;

    public AccessTokenResponse login(String username, String password, HttpServletResponse response){
        try {
            // 1. 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // 2. 인증 성공 후 UserDetails 가져오기
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // 3. 토큰 발급
            int userVersion = redisTokenService.currentUserVersion(userDetails.getUsername());
            RoleType roleType = userDetails.getRoleType();
            String accessToken = jwtUtil.createAccessToken(userDetails.getUsername(), userVersion, "access", roleType);   //accessToken
            String refreshToken = jwtUtil.createRefreshToken(userDetails.getUsername(), "refresh", roleType);             //refreshToken
            long accessTokenTtl = accessMinutes * 60;

            // 4. Redis에 리프레시 토큰 저장 & 쿠키에 토큰 저장
            long ttlSec = refreshDays * 24 * 60 * 60;
            redisTokenService.saveRefreshToken(userDetails.getUsername(), refreshToken, ttlSec);
            CookieUtil.setRefreshCookie(response, refreshToken, (int) ttlSec);

            //response.setHeader("accessToken", accessToken);

            return new AccessTokenResponse(accessToken, accessTokenTtl);
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }
}
