package com.eneifour.fantry.security.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

//Refresh 토큰을 http only cookie로 저장
public class CookieUtil {

    //쿠키 생성
    public static void setRefreshCookie(HttpServletResponse response, String token, int maxAgeSec) {
        Cookie cookie = new Cookie("refreshToken",token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");    //모든 경로에서 쿠키 사용가능
        cookie.setMaxAge(maxAgeSec);
        response.addCookie(cookie);
    }

    //쿠키 삭제
    public static void clearRefreshCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken",null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
