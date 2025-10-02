package com.eneifour.fantry.security.controller;

import com.eneifour.fantry.member.dto.MemberDTO;
import com.eneifour.fantry.security.dto.TokenResponse;
import com.eneifour.fantry.security.dto.loginResponse;
import com.eneifour.fantry.security.model.LoginService;
import com.eneifour.fantry.security.model.LogoutService;
import com.eneifour.fantry.security.model.ReissueService;
import com.eneifour.fantry.security.util.CookieUtil;
import com.eneifour.fantry.security.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;
    private final JwtUtil jwtUtil;
    private final ReissueService reissueService;
    @Value("${spring.jwt.refresh-days}") long refreshTtl;
    private final LogoutService logoutService;

    //로그인 요청 처리
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDTO memberDTO, HttpServletResponse response) {
        // 서비스 메서드 호출
        loginResponse accessToken = loginService.login(memberDTO.getUsername(), memberDTO.getPassword(), response);
        return ResponseEntity.ok(accessToken);
    }

    //Refresh 토큰으로 새 Access 토큰 발급
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        TokenResponse tokenResponse = reissueService.reissueAccessToken(request);

        //헤더+쿠키 설정
        response.setHeader("accessToken", tokenResponse.getAccessToken());
        int refreshTtlSec = (int)(refreshTtl * 24 * 60 * 60);
        CookieUtil.setRefreshCookie(response, tokenResponse.getRefreshToken(), refreshTtlSec);

        return ResponseEntity.ok().body(tokenResponse.getAccessToken());
    }

    // 로그아웃 요청 처리
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        logoutService.logout(request, response);
        return ResponseEntity.ok(Map.of("result", "로그아웃 성공"));
    }
}
