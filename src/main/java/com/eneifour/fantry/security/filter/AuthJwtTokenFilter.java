package com.eneifour.fantry.security.filter;

import com.eneifour.fantry.security.exception.exception.InvalidTokenTypeException;
import com.eneifour.fantry.security.exception.exception.TimeoutAccessTokenException;
import com.eneifour.fantry.security.exception.exception.TokenHeaderVerificationException;
import com.eneifour.fantry.security.exception.exception.UnauthorizedException;
import com.eneifour.fantry.security.model.RedisTokenService;
import com.eneifour.fantry.security.util.JwtUtil;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class AuthJwtTokenFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final RedisTokenService redis;

    public AuthJwtTokenFilter(JwtUtil jwtUtil, RedisTokenService redis) {
        this.jwtUtil = jwtUtil;
        this.redis = redis;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException, TimeoutAccessTokenException, InvalidTokenTypeException, UnauthorizedException {
        //로그인이 필요없은 요청은 이 필터를 건너뛰기
        List<String> passFilterUrls = Arrays.asList("/api/login", "/api/reissue", "/api/user");
        String requestURI = request.getRequestURI();

        if(passFilterUrls.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            //요청 헤더에서 JWT 추출
            String authorizationHeader = request.getHeader("Authorization");
            //넘어온 헤더 값이 정상
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String accessToken = authorizationHeader.substring(7);

                //JWT 파싱 및 서명 조작여부, 기간 만료 검증
                Jws<Claims> jws = jwtUtil.parseToken(accessToken);
                Claims claims = jws.getBody();

                //accessToken인가?
                String category = claims.get("category", String.class);
                if (!"access".equals(category)) {
                    throw new InvalidTokenTypeException("Access Token이 아닙니다.");
                }

                //토큰 만료 여부 확인
                if(!"/api/logout".equals(requestURI)) {
                    Date expiration = claims.getExpiration();
                    if(expiration == null || expiration.before(new Date())) {
                        throw new TimeoutAccessTokenException("토큰 시간이 만료됨");
                    }
                }

                //Claimes 값 추출, 블랙리스트 여부 판단
                String tokenId = jwtUtil.getJwtId(claims);
                String username = jwtUtil.getUsername(claims);
                int ver = jwtUtil.getVersion(claims);

                if(username == null || tokenId == null){
                    throw new InvalidTokenTypeException("토큰 정보가 누락되었습니다.");
                }

                if(redis.isBlackList(tokenId)){
                    throw new UnauthorizedException("권한이 없습니다.");
                }

                int currentVer = redis.currentUserVersion(username);
                if(currentVer != ver){
                    throw new UnauthorizedException("버전이 일치하지 않습니다.");
                }

                //인증 객체
                String role = claims.get("role", String.class);
                GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+role);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,null, List.of(authority));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                //정상 헤더값이 아님
                throw new TokenHeaderVerificationException("Authorization 헤더가 없거나 혹은 잘못된 형식입니다.");
            }
            filterChain.doFilter(request, response);

        }catch (ExpiredJwtException e){
            if(requestURI.contains("/logout")) {
                Claims expiredClaims = e.getClaims();
                // 만료된 토큰이지만 기본 검증은 수행
                String category = expiredClaims.get("category", String.class);
                if (!"access".equals(category)) {
                    throw new InvalidTokenTypeException("Access Token이 아닙니다.");
                }

                String tokenId = jwtUtil.getJwtId(expiredClaims);
                String username = jwtUtil.getUsername(expiredClaims);

                if(username == null || tokenId == null){
                    throw new InvalidTokenTypeException("토큰 정보가 누락되었습니다.");
                }

                // 인증 객체 생성 (만료되었지만 로그아웃 처리를 위해)
                String role = expiredClaims.get("role", String.class);
                GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+role);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,null, List.of(authority));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                filterChain.doFilter(request, response);
            }else{
                throw new TimeoutAccessTokenException("AccessToken이 만료되었습니다.");
            }
        }catch (UnsupportedJwtException e) {
            throw new InvalidTokenTypeException("지원하지 않는 JWT 형식입니다.");
        }catch (MalformedJwtException e) {
            throw new InvalidTokenTypeException("잘못된 JWT 형식입니다.");
        }catch (SignatureException e) {
            throw new UnauthorizedException("JWT 서명 검증에 실패하였습니다.");
        }catch (JwtException e){
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
        catch (Exception e){
            //기타 예외
            throw new UnauthorizedException("인증 에러가 발생했습니다: " + e.getMessage());
        }
    }
}