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

    private TokenMemberResponse(int memberId, String id, String password, String name, String email, String tel, String role) {
        this.memberId = memberId;
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.role = role;
    }

    public static TokenMemberResponse from(Member member) {
        return new TokenMemberResponse(
                member.getMemberId(),
                member.getId(),
                member.getPassword(),
                member.getName(),
                member.getEmail(),
                member.getTel(),
                String.valueOf(member.getRole().getRoleType())
        );
    }
}
