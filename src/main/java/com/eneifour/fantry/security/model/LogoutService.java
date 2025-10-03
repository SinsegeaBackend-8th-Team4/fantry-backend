package com.eneifour.fantry.security.model;

import com.eneifour.fantry.security.exception.AuthErrorCode;
import com.eneifour.fantry.security.exception.AuthException;
import com.eneifour.fantry.security.util.CookieUtil;
import com.eneifour.fantry.security.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

//로그아웃을 관리하는 서비스
@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutService {

    private final JwtUtil jwtUtil;
    private final RedisTokenService redis;

    public void logout(HttpServletRequest request, HttpServletResponse response) throws AuthException {
        // 1. 로그아웃 요청인지 확인
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        if(!requestURI.equals("^\\/logout$") && !requestMethod.equals("POST")) {
            throw new AuthException(AuthErrorCode.AUTH_NOT_LOGIN_REQUEST);
        }

        // 2. 쿠키에서 리프레시 토큰 가져오기
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("refreshToken")){
                    refresh = cookie.getValue();
                    break;
                }
            }
        }

        // 3. 유효성 체크
        //refreshToken 존재 확인
        if(refresh == null){
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_MISSING);
        }

        //Refresh Token 만료 여부 체크
        Claims claims = null;
        try{
            Jws<Claims> jws = jwtUtil.parseToken(refresh);
            claims = jws.getBody();
        }catch(ExpiredJwtException e){
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_EXPIRED);
        }catch(JwtException e){
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_INVALID);
        }

        //category 확인(발급 시 페이로드에 명시)
        if(!"refresh".equals(jwtUtil.getCategory(claims))){
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_MISSING);
        }

        //Redis에 RefreshToken 존재 여부
        String username = jwtUtil.getUsername(claims);
        if(!redis.isExistRefreshToken(username)){
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_NOT_IN_STORE);
        }

        // 4. 쿠키에서 refreshToken 삭제
        CookieUtil.clearRefreshCookie(response);
        log.debug("로그아웃 요청으로 쿠키에서 토큰을 삭제했습니다.");

        // 5. Redis에서 refreshToken 삭제
        redis.deleteRefreshToken(username);

        // 6. 블랙리스트에 accessToken 등록
        String accessToken = jwtUtil.extractAccessToken(request);
        Claims accessClaims = null;
        try {
            Jws<Claims> accessJws = jwtUtil.parseToken(accessToken);
            accessClaims = accessJws.getBody();
        } catch (ExpiredJwtException e) {
            accessClaims = e.getClaims();
        }
        String tokenId = jwtUtil.getJwtId(accessClaims);

        long accessTtl = jwtUtil.getRemainTime(accessClaims);
        //이미 만료된 토큰이면 짧은 TTL로 설정(1분)
        if(accessTtl <= 0) accessTtl = 60;

        redis.saveBlackList(tokenId, accessToken, accessTtl);
    }
}
