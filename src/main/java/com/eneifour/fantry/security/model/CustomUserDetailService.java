package com.eneifour.fantry.security.model;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.model.JpaMemberRepository;
import com.eneifour.fantry.security.dto.CustomUserDetails;
import com.eneifour.fantry.security.exception.AuthErrorCode;
import com.eneifour.fantry.security.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final JpaMemberRepository jpaMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws AuthException {
        //DB 조회
        Member member = jpaMemberRepository.findById(username);

        if(member == null){
            throw new AuthException(AuthErrorCode.TOKEN_USER_NOT_NOT_FOUND);
        }
        return new CustomUserDetails(member);
    }
}
