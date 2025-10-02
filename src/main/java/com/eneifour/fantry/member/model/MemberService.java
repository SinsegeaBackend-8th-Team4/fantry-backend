package com.eneifour.fantry.member.model;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.domain.Role;
import com.eneifour.fantry.member.exception.MemberErrorCode;
import com.eneifour.fantry.member.exception.MemberException;
import com.eneifour.fantry.security.dto.MemberResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    //모든 회원 가져오기
    public List<Member> getMembers(){
        return jpaMemberRepository.findAll();
    }

    //하나의 회원 가져오기
    public Member getMemberById(String id){
        return jpaMemberRepository.findById(id);
    }

    //회원 추가하기
    public void saveMember(Member member){
        jpaMemberRepository.save(member);
    }

    //회원 수정하기
    @Transactional
    public Member updateMember(Member member) throws MemberException {
        if(!jpaMemberRepository.existsById(member.getId())){
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
        return jpaMemberRepository.save(member);
    }

    //회원 삭제하기
    @Transactional
    public void deleteMemberById(String id) throws MemberException {
        if(!jpaMemberRepository.existsById(id)){
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
        jpaMemberRepository.deleteById(id);
    }

    //한명의 회원의 권한을 수정하기
    @Transactional
    public void updateMemberRole(String id, Role role) throws MemberException {
        Member member = jpaMemberRepository.findById(id);
        if(member == null){
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
        member.setRole(role);
    }
}
