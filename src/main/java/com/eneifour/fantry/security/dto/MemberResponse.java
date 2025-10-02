package com.eneifour.fantry.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResponse {
    private int memberId;
    private String id;
    private String password;
    private String name;
    private String email;
    private String tel;
    private String role;
}
