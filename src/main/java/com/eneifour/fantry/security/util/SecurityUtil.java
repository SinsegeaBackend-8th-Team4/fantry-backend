package com.eneifour.fantry.security.util;

import com.eneifour.fantry.security.exception.AuthErrorCode;
import com.eneifour.fantry.security.exception.AuthException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    // 현재 로그인한 사용자의 ID(username)을 반환한다.
    public static String getUserName(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getName() == null){
            throw new AuthException(AuthErrorCode.AUTH_UNEXPECTED_ERROR);
        }
        return authentication.getName();
    }
}
