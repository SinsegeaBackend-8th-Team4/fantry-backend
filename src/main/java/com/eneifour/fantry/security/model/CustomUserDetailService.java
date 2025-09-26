package com.eneifour.fantry.security.model;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.model.JpaMemberRepository;
import com.eneifour.fantry.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final JpaMemberRepository jpaMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //DB 조회
        Member member = jpaMemberRepository.findById(username);

        if(member == null){
            throw new UsernameNotFoundException("일치하는 회원이 존재하지 않습니다.");
        }
        return new CustomUserDetails(member);
    }
}
