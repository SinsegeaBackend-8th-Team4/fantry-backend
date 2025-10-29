package com.eneifour.fantry.member.service;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.domain.Role;
import com.eneifour.fantry.member.dto.MemberCreateRequest;
import com.eneifour.fantry.member.dto.MemberResponse;
import com.eneifour.fantry.member.dto.MemberUpdateRequest;
import com.eneifour.fantry.member.exception.MemberErrorCode;
import com.eneifour.fantry.member.exception.MemberException;
import com.eneifour.fantry.member.repository.JpaMemberRepository;
import com.eneifour.fantry.member.repository.RoleRepository;
import com.eneifour.fantry.address.repository.AddressRepository;
import com.eneifour.fantry.account.repository.AccountRepository;
import com.eneifour.fantry.address.domain.Address;
import com.eneifour.fantry.account.domain.Account;
import com.eneifour.fantry.security.dto.TokenMemberResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final JpaMemberRepository jpaMemberRepository;
    private final RoleRepository roleRepository;
    private final AddressRepository addressRepository;
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PasswordEncoder passwordEncoder;

    //토큰으로 회원 찾기
    public TokenMemberResponse findMemberResponseBy(String username) {
        Member member = jpaMemberRepository.findById(username);
        if(member == null) return null;

        // 기본 배송지 조회(isDefault == '1')
        String roadAddress = null;
        String detailAddress = null;
        try {
            Address addr = addressRepository.findByMember_MemberIdAndIsDefault(member.getMemberId(), '1');
            if (addr != null) {
                String dest = addr.getDestinationAddress();
                if (dest != null && dest.contains("##")) {
                    String[] parts = dest.split("##", 2);
                    roadAddress = parts[0];
                    detailAddress = parts[1];
                } else {
                    roadAddress = dest;
                }
            }
        } catch(Exception ignored) {}

        // 활성 계좌 조회(isActive == '1')
        String accountNumber = null;
        String bankName = null;
        try {
            Account acct = accountRepository.findByMember_MemberIdAndIsActive(member.getMemberId(), '1');
            if (acct != null) {
                accountNumber = acct.getAccountNumber();
                bankName = acct.getBankName();
            }
        } catch(Exception ignored) {}

        return TokenMemberResponse.from(member, roadAddress, detailAddress, accountNumber, bankName);
    }

    //모든 회원 가져오기
    public List<MemberResponse> getMembers(){
        List<Member> members = jpaMemberRepository.findAll();
        return MemberResponse.of(members);
    }

    //하나의 회원 가져오기
    public MemberResponse getMemberById(String id) throws MemberException {
        Member member = jpaMemberRepository.findById(id);
        if(member == null){
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
        return new MemberResponse(member);
    }

    //회원 추가하기
    public void saveMember(MemberCreateRequest memberRequest) throws MemberException {
        if(jpaMemberRepository.existsById(memberRequest.getId())){
            throw new MemberException(MemberErrorCode.MEMBER_ID_DUPLICATED);
        }
        Role role = roleRepository.findById(memberRequest.getRoleId()).orElseThrow(() -> new MemberException(MemberErrorCode.ROLE_NOT_FOUND));
        Member member = memberRequest.toEntity(role);
        member.setPassword(bCryptPasswordEncoder.encode(memberRequest.getPassword()));
        jpaMemberRepository.save(member);
    }

    //회원 수정하기
    @Transactional
    public void updateMember(MemberUpdateRequest memberUpdateRequest) throws MemberException {
        Member member = jpaMemberRepository.findById(memberUpdateRequest.getId());
        if(member == null){
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
        memberUpdateRequest.applyTo(member);
        //return jpaMemberRepository.save(member);
    }

    //회원 삭제하기
    @Transactional
    public void deleteMemberById(String id) throws MemberException {
        if(!jpaMemberRepository.existsById(id)){
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
        jpaMemberRepository.deleteById(id);
    }

    //회원 삭제하기(플래그 변경)
    @Transactional
    public void deactiveateMember(String id) {
        Member member = jpaMemberRepository.findById(id);
        if(member == null){
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
        member.setIsActive(1);
        jpaMemberRepository.save(member);
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

    //마이페이지 2차 인증을 위한 비밀번호 일치 여부 검증
    public boolean verifyPassword(String id, String password) throws MemberException {
        Member member = jpaMemberRepository.findById(id);
        if(member == null){
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
        if(passwordEncoder.matches(password, member.getPassword())){
            //비밀번호 일치: 2차 인증 성공
            return true;
        }else{
            throw new MemberException(MemberErrorCode.MEMBER_INVALID_PASSWORD);
        }
    }
}
