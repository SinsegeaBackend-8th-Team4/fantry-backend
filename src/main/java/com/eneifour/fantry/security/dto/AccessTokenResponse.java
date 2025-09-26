package com.eneifour.fantry.security.dto;

import lombok.Getter;

@Getter
public class AccessTokenResponse {
    private String accessToken;
    private long expSec;

    public AccessTokenResponse(String accessToken, long expSec) {
        this.accessToken = accessToken;
        this.expSec = expSec;
    }
}
