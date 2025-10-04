package com.eneifour.fantry.member.dto;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.domain.RoleType;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;


//조회를 위한 DTO
@Getter
public class MemberResponseDTO {
    private final int memberId;
    private final String id;
    private final String password;
    private final String name;
    private final String email;
    private final String tel;
    private final String createAt;
    private final String leavedAt;
    private final int isActive;
    private final RoleType roleType;

    public MemberResponseDTO(Member member) {
        this.memberId = member.getMemberId();
        this.id = member.getId();
        this.password = member.getPassword();
        this.name = member.getName();
        this.email = member.getEmail();
        this.tel = member.getTel();
        this.createAt = member.getCreateAt();
        this.leavedAt = member.getLeavedAt();
        this.isActive = member.getIsActive();
        this.roleType = member.getRole().getRoleType();
    }

    // Entity 리스트를 DTO 리스트로 변환하는 정적 팩토리 메서드
    public static List<MemberResponseDTO> of(List<Member> members) {
        return members.stream()
                .map(MemberResponseDTO::new)
                .collect(Collectors.toList());
    }
}
