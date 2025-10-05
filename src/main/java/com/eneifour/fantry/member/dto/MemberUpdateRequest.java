package com.eneifour.fantry.member.dto;

import com.eneifour.fantry.member.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateRequest {
    private String id;
    private String password;
    private String name;
    private String tel;
    private String email;

    public void applyTo(Member member){
        member.setId(this.id);
        member.setPassword(this.password);
        member.setName(this.name);
        member.setTel(this.tel);
        member.setEmail(this.email);
    }
}
