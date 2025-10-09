package com.eneifour.fantry.member.dto;

import com.eneifour.fantry.member.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateRequest {
    private String id;
    private String name;
    private String tel;
    private String email;
    private int isActive;

    public void applyTo(Member member){
        member.setId(this.id);
        member.setName(this.name);
        member.setTel(this.tel);
        member.setEmail(this.email);
        member.setIsActive(this.isActive);
    }
}
