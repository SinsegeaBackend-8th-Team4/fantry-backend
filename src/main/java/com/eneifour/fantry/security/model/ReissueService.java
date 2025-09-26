package com.eneifour.fantry.security.model;

import com.eneifour.fantry.member.domain.RoleType;
import com.eneifour.fantry.security.dto.TokenResponse;
import com.eneifour.fantry.security.exception.exception.JwtRefreshTokenException;
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
    @Value("${spring.jwt.refresh-days}") long refreshDays;

    public TokenResponse reissueAccessToken(HttpServletRequest request){
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
         throw new JwtRefreshTokenException("refresh token is null: 토큰이 존재하지 않습니다.");
        }

        //Refresh Token 만료 여부 체크
        Claims claims = null;
        try{
            Jws<Claims> jws = jwtUtil.parseToken(refresh);
            claims = jws.getBody();
        }catch(ExpiredJwtException e){
            throw new JwtRefreshTokenException("invalid refresh token: 리프레시 토큰이 만료되었습니다.");
        }catch(JwtException e){
            throw new JwtRefreshTokenException("유효하지 않은 리프레시 토큰입니다.");
        }

        //category 확인(발급 시 페이로드에 명시)
        if(!"refresh".equals(jwtUtil.getCategory(claims))){    //카테고리 얻어오는 함수 정의해야 함
            throw new JwtRefreshTokenException("invalid refresh token: 저장된 토큰이 없습니다.");
        }

        //Redis에 저장되어 있는지 확인(블랙리스트)
        String username = jwtUtil.getUsername(claims);
        String userId = jwtUtil.getUsername(claims);
        String tokenId = jwtUtil.getJwtId(claims);

        if(!redisTokenService.matchesRefreshToken(userId, refresh)){
            throw new JwtRefreshTokenException("invalid refresh token: 저장된 토큰과 일치하지 않습니다.");
        }

        if(redisTokenService.isBlackList(tokenId)){
            throw new JwtRefreshTokenException("해당 토큰은 사용이 금지되어 있습니다.");
        }

        //토큰에서 정보를 꺼내고 새 AccessToken 발급하기
        int currentVer = redisTokenService.currentUserVersion(username);
        RoleType roleType = jwtUtil.getRole(claims);

        String newAccessToken = jwtUtil.createAccessToken(username, currentVer, "access", roleType);
        String newRefreshToken = jwtUtil.createRefreshToken(username, "refresh", roleType);

        //Refresh Token 저장, 기존의 Refresh 토큰 삭제 후 새 토큰 저장 -> 수정 필요!!!
        redisTokenService.deleteRefreshToken(userId);
//        String newTokenId = jwtUtil.getJwtId(jwtUtil.parseToken(newRefreshToken).getBody());
        long tokenTtl = refreshDays * 24 * 60 * 60;
        redisTokenService.saveRefreshToken(username, newRefreshToken, tokenTtl);

        //새로 발급한 AccessToken과 RefreshToken 응답
        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}
