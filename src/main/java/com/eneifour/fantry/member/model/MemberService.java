package com.eneifour.fantry.member.model;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.security.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final JpaMemberRepository jpaMemberRepository;

    public MemberResponse findMemberResponseBy(String username) {
        Member member = jpaMemberRepository.findById(username);

        MemberResponse memberResponse = new MemberResponse();
        memberResponse.setMemberId(member.getMemberId());
        memberResponse.setId(member.getId());
        memberResponse.setPassword(member.getPassword());
        memberResponse.setName(member.getName());
        memberResponse.setEmail(member.getEmail());
        memberResponse.setTel(member.getTel());
        memberResponse.setRole(String.valueOf(member.getRole().getRoleType()));

        return memberResponse;
    }
}
