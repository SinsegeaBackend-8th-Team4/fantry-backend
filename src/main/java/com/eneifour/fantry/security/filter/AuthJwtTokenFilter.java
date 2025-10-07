package com.eneifour.fantry.security.filter;

import com.eneifour.fantry.security.config.SecurityConstants;
import com.eneifour.fantry.security.exception.AuthErrorCode;
import com.eneifour.fantry.security.exception.AuthException;
import com.eneifour.fantry.security.service.CustomUserDetailService;
import com.eneifour.fantry.security.service.RedisTokenService;
import com.eneifour.fantry.security.util.JwtUtil;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor // @author 재환
public class AuthJwtTokenFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final RedisTokenService redis;
    private final CustomUserDetailService userDetailService; //@author 재환

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException, AuthException {

        // Security Config에서 permitAll 요청은 해당 필터를 거치지 않으므로 제거하였습니다.
        String requestURI = request.getRequestURI();
        AntPathMatcher matcher = new AntPathMatcher();
        for (String pattern : SecurityConstants.PUBLIC_URIS) {
            if (matcher.match(pattern, requestURI)) {
                filterChain.doFilter(request, response);
                return;
            }
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
                    throw new AuthException(AuthErrorCode.TOKEN_NOT_ACCESS_TOKEN);
                }

                //토큰 만료 여부 확인
                if(!"/api/logout".equals(requestURI)) {
                    Date expiration = claims.getExpiration();
                    if(expiration == null || expiration.before(new Date())) {
                        throw new AuthException(AuthErrorCode.TOKEN_EXPIRED);
                    }
                }

                //Claimes 값 추출, 블랙리스트 여부 판단
                String tokenId = jwtUtil.getJwtId(claims);
                String username = jwtUtil.getUsername(claims);
                int ver = jwtUtil.getVersion(claims);

                if(username == null || tokenId == null){
                    throw new AuthException(AuthErrorCode.TOKEN_MISSING);
                }

                if(redis.isBlackList(tokenId)){
                    throw new AuthException(AuthErrorCode.TOKEN_BLACKLISTED);
                }

                int currentVer = redis.currentUserVersion(username);
                if(currentVer != ver){
                    throw new AuthException(AuthErrorCode.AUTH_VERSION_MISMATCH);
                }

                UserDetails userDetails = userDetailService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                //정상 헤더값이 아님
                throw new AuthException(AuthErrorCode.TOKEN_HEADER_INVALID);
            }
            filterChain.doFilter(request, response);

        }catch (ExpiredJwtException e){
            if(requestURI.contains("/logout")) {
                Claims expiredClaims = e.getClaims();
                // 만료된 토큰이지만 기본 검증은 수행
                String category = expiredClaims.get("category", String.class);
                if (!"access".equals(category)) {
                    throw new AuthException(AuthErrorCode.TOKEN_NOT_ACCESS_TOKEN);
                }

                String tokenId = jwtUtil.getJwtId(expiredClaims);
                String username = jwtUtil.getUsername(expiredClaims);

                if(username == null || tokenId == null){
                    throw new AuthException(AuthErrorCode.TOKEN_MISSING);
                }

                // 인증 객체 생성 (만료되었지만 로그아웃 처리를 위해)
                String role = expiredClaims.get("role", String.class);
                GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+role);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,null, List.of(authority));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                filterChain.doFilter(request, response);
            }else{
                throw new AuthException(AuthErrorCode.TOKEN_EXPIRED);
            }
        }catch (UnsupportedJwtException e) {
            throw new AuthException(AuthErrorCode.TOKEN_UNSUPPORTED_FORMAT);
        }catch (MalformedJwtException e) {
            throw new AuthException(AuthErrorCode.TOKEN_MALFORMED_FORMAT);
        }catch (SignatureException e) {
            throw new AuthException(AuthErrorCode.TOKEN_SIGNATURE_INVALID);
        }catch (JwtException e){
            throw new AuthException(AuthErrorCode.TOKEN_INVALID);
        }
        catch (Exception e){
            //기타 예외
            log.error("알 수 없는 인증 에러가 발생했습니다.", e);
            throw new AuthException(AuthErrorCode.AUTH_UNEXPECTED_ERROR);
        }
    }
}