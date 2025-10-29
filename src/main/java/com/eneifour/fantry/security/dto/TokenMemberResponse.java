package com.eneifour.fantry.security.dto;

import com.eneifour.fantry.member.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TokenMemberResponse {
    private int memberId;
    private String id;
    private String password;
    private String name;
    private String email;
    private String tel;
    private String role;
    // 추가 필드: 기본 배송지와 활성 계좌 정보
    private String roadAddress;
    private String detailAddress;
    private String accountNumber;
    private String bankName;

    private TokenMemberResponse(int memberId, String id, String password, String name, String email, String tel, String role,
                                String roadAddress, String detailAddress, String accountNumber, String bankName) {
        this.memberId = memberId;
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.role = role;
        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
    }

    public static TokenMemberResponse from(Member member) {
        return new TokenMemberResponse(
                member.getMemberId(),
                member.getId(),
                member.getPassword(),
                member.getName(),
                member.getEmail(),
                member.getTel(),
                String.valueOf(member.getRole().getRoleType()),
                null, null, null, null
        );
    }

    // 확장된 팩토리 메서드: 주소/계좌 정보를 함께 제공
    public static TokenMemberResponse from(Member member, String roadAddress, String detailAddress,
                                           String accountNumber, String bankName) {
        return new TokenMemberResponse(
                member.getMemberId(),
                member.getId(),
                member.getPassword(),
                member.getName(),
                member.getEmail(),
                member.getTel(),
                String.valueOf(member.getRole().getRoleType()),
                roadAddress, detailAddress, accountNumber, bankName
        );
    }
}
