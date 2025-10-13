package com.eneifour.fantry.security.service;

import com.eneifour.fantry.member.domain.RoleType;
import com.eneifour.fantry.security.dto.TokenResponse;
import com.eneifour.fantry.security.exception.AuthErrorCode;
import com.eneifour.fantry.security.exception.AuthException;
import com.eneifour.fantry.security.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//클라이언트가 쿠키로 보낸 Refresh Token의 유효성을 확인하여 새로운 Access Token 발급
@Slf4j
@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JwtUtil jwtUtil;
    private final RedisTokenService redisTokenService;
    @Value("${spring.jwt.refresh-hour}") long refreshHour;

    public TokenResponse reissueAccessToken(HttpServletRequest request) throws AuthException {
        //Refresh Token 꺼내기
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

        //Refresh Token 존재 확인
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
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_NOT_SAVE);
        }

        //Redis에 저장되어 있는지 확인
        String username = jwtUtil.getUsername(claims);

        if(!redisTokenService.matchesRefreshToken(username, refresh)){
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_MISMATCH);
        }

        //accessToken이 블랙리스트에 저장되어 있는지 확인
        String accessToken = jwtUtil.extractAccessToken(request);
        Claims accessClaims = null;
        try {
            Jws<Claims> accessJws = jwtUtil.parseToken(accessToken);
            accessClaims = accessJws.getBody();
        } catch (ExpiredJwtException e) {
            accessClaims = e.getClaims();
        }
        String tokenId = jwtUtil.getJwtId(accessClaims);

        if(redisTokenService.isBlackList(tokenId)){
            throw new AuthException(AuthErrorCode.TOKEN_BLACKLISTED);
        }

        //토큰에서 정보를 꺼내고 새 AccessToken 발급하기
        int currentVer = redisTokenService.currentUserVersion(username);
        RoleType roleType = jwtUtil.getRole(claims);

        String newAccessToken = jwtUtil.createAccessToken(username, currentVer, "access", roleType);
        String newRefreshToken = jwtUtil.createRefreshToken(username, "refresh", roleType);

        //Refresh Token 저장, 기존의 Refresh 토큰 삭제 후 새 토큰 저장
        redisTokenService.deleteRefreshToken(username);
//        String newTokenId = jwtUtil.getJwtId(jwtUtil.parseToken(newRefreshToken).getBody());
        long tokenTtl = refreshHour * 60 * 60;
        redisTokenService.saveRefreshToken(username, newRefreshToken, tokenTtl);

        //새로 발급한 AccessToken과 RefreshToken 응답
        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}
