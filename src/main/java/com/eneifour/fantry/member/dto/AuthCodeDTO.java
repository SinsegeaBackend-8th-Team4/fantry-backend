package com.eneifour.fantry.member.dto;

import lombok.Getter;

@Getter
public class AuthCodeDTO {
    private String email;
    private String code;

    public void setEmail(String email) {
        this.email = email;
    }
    public void setCode(String code) {
        this.code = code;
    }
}
