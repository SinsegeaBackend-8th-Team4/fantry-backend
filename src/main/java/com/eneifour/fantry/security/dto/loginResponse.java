package com.eneifour.fantry.security.dto;

import lombok.Getter;

@Getter
public class loginResponse {
    private String accessToken;
    private long expSec;
    private MemberResponse memberResponse;

    public loginResponse(String accessToken, long expSec, MemberResponse memberResponse) {
        this.accessToken = accessToken;
        this.expSec = expSec;
        this.memberResponse = memberResponse;
    }
}
