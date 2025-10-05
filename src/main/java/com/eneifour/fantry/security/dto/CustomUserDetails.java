package com.eneifour.fantry.security.dto;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.domain.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//사용자 정보를 인증하고 권한을 부여하는데 필요한 정보를 제공하는 객체
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final Member member;

    //사용자 권한
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+member.getRole().getRoleType().name()));
    }

    //사용자 비밀번호 반환, Security 인증 시 이 비밀번호와 입력된 비밀번호를 비교
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    //사용자 이름(id)을 반환, Security 인증 시 이 이름과 입력된 이름을 비교
    @Override
    public String getUsername() {
        return member.getId();
    }

    //멤버 pk 가져오기
    public int getMemberId(){
        return member.getMemberId();
    }

    //사용자 이메일 가져오기
    public String getEmail() {
        return member.getEmail();
    }

    //권한명 가져오기
    public RoleType getRoleType() {
        return member.getRole().getRoleType();
    }
}
