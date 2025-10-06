package com.eneifour.fantry.member.dto;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.domain.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCreateRequest {
    private String id;
    private String password;
    private String name;
    private String email;
    private String tel;
    private String sns;
    private int roleId;

    public Member toEntity(Role role) {
        Member member = new Member();
        member.setId(this.id);
        member.setPassword(this.password);
        member.setName(this.name);
        member.setEmail(this.email);
        member.setTel(this.tel);
        member.setSns(this.sns);
        member.setRole(role);
        member.setIsActive(1); // 가입 시 기본 활성 상태
        return member;
    }
}
