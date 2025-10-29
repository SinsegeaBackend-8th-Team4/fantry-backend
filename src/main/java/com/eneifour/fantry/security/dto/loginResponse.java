package com.eneifour.fantry.security.dto;

import lombok.Getter;

@Getter
public class loginResponse {
    private String accessToken;
    private long expSec;
    private TokenMemberResponse tokenMemberResponse;

    public loginResponse(String accessToken, long expSec, TokenMemberResponse tokenMemberResponse) {
        this.accessToken = accessToken;
        this.expSec = expSec;
        this.tokenMemberResponse = tokenMemberResponse;
    }
}
