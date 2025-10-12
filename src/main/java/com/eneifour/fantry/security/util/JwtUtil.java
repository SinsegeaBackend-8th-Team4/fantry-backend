package com.eneifour.fantry.security.util;

import com.eneifour.fantry.member.domain.RoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

//JWT를 생성하고 검증 및 정보를 추출하는 클래스
@Component
public class JwtUtil {
    private SecretKey secretKey;    //JWT 서명에 사용될 비밀키(HMAC-SHA)
    private String issure;          //발급자(토큰 생성자)
    private long accessMinutes;     //AccessToken 만료기간(분)
    private long refreshHour;       //RefreshToken 만료기간(시간)

    //생성자 주입
    public JwtUtil(@Value("${spring.jwt.secret}")String secret,
                   @Value("${spring.jwt.issure}")String issure,
                   @Value("${spring.jwt.access-minutes}")long accessMinutes,
                   @Value("${spring.jwt.refresh-hour}")long refreshHour) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issure = issure;
        this.accessMinutes = accessMinutes;
        this.refreshHour = refreshHour;
    }

    /* --------------------------------------------------------------------
     *  JWT 토큰 생성
     * --------------------------------------------------------------------*/
    // AccessToken 생성
    public String createAccessToken(String username, int userVersion, String category, RoleType roleType) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessMinutes*60);
        
        String token = Jwts.builder()
                .issuer(issure)
                .subject(username)
                .id(UUID.randomUUID().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .claim("ver", userVersion)
                .claim("category", category)
                .claim("role", roleType)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
        return token;
    }

    //RefreshToken 생성
    public String createRefreshToken(String username, String category, RoleType roleType) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(refreshHour*60*60);

        String token = Jwts.builder()
                .issuer(issure)
                .subject(username)
                .id(UUID.randomUUID().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .claim("category", category)
                .claim("role", roleType)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();

        return token;
    }

    /* --------------------------------------------------------------------
     *  토큰 파싱 및 값 추출
     * -------------------------------------------------------------------*/
    public Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
    }

    //JWT에서 username 값(id) 을 추출
    public String getUsername(Claims claims) {
        return claims.getSubject();
    }

    //JWT에서 token 고유 id 값을 추출
    public String getJwtId(Claims claims) {
        return claims.getId();
    }

    //JWT에서 버전 값을 추출
    public int getVersion(Claims claims) {
        Object ver = claims.get("ver");
        return (ver == null) ? 0 : (Integer) ver;
    }

    //JWT에서 만료시간 추출
    public long getExpireTime(Claims claims) {
        Date exp = claims.getExpiration();
        return (exp == null) ? 0L : exp.toInstant().getEpochSecond();
    }

    //Role 추출
    public RoleType getRole(Claims claims) {
        String roleStr = claims.get("role", String.class);
        return (roleStr == null) ? null : RoleType.valueOf(roleStr);
    }

    //카테고리 추출
    public String getCategory(Claims claims) {
        Object category = claims.get("category");
        return (category == null) ? null : category.toString();
    }

    //accessToken 헤더에서 가져오기
    public String extractAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }
        return null;
    }

    //accessToken 만료시간까지 남은 시간
    public long getRemainTime(Claims claims) {
        Date exp = claims.getExpiration();
        if(exp == null) return 0L;

        long expireTime = exp.toInstant().getEpochSecond();
        long currentTime = Instant.now().getEpochSecond();
        long remainTime = expireTime - currentTime;

        return Math.max(0L, remainTime);
    }
}
