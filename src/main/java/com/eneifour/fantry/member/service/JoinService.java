package com.eneifour.fantry.member.service;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.domain.Role;
import com.eneifour.fantry.member.domain.RoleType;
import com.eneifour.fantry.member.dto.MemberDTO;
import com.eneifour.fantry.member.exception.MemberErrorCode;
import com.eneifour.fantry.member.exception.MemberException;
import com.eneifour.fantry.member.repository.JpaMemberRepository;
import com.eneifour.fantry.member.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final RoleRepository roleRepository;
    private final JpaMemberRepository jpaMemberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //아이디 중복 체크
    public boolean isIdDuplicated(String id) {
        return jpaMemberRepository.existsById(id);
    }

    //이메일 중복 체크
    public Member isEmailDuplicated(String email) {
        return jpaMemberRepository.findByEmail(email);
    }

    //회원가입
    @Transactional
    public void join(MemberDTO memberDTO, RoleType default_role) throws MemberException {
        String username = memberDTO.getUsername();
        String password = memberDTO.getPassword();

        //이미 존재하는 계정 가입 막기
        if (isIdDuplicated(username)) {
            return;
        }

        if (isEmailDuplicated(memberDTO.getEmail()) != null){
            throw new MemberException(MemberErrorCode.MEMBER_EMAIL_DUPLICATED);
        }

        //Member 모델에 할당
        Member member = new Member();
        member.setId(username);
        member.setPassword(bCryptPasswordEncoder.encode(password));
        member.setName(memberDTO.getName());
        member.setEmail(memberDTO.getEmail());
        member.setTel(memberDTO.getPhone());

        Role role = roleRepository.findByRoleType(default_role);
        if(role == null){
            throw new MemberException(MemberErrorCode.MEMBER_ROLE_NOT_FOUND);
        }
        member.setRole(role);

        //저장
        jpaMemberRepository.save(member);
    }

    //아이디 찾기
    public MemberDTO findMemberByEmail(String email) {
        Member member = jpaMemberRepository.findByEmail(email);
        if (member == null) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUsername(member.getId());
        memberDTO.setEmail(member.getEmail());

        return memberDTO;
    }
}
