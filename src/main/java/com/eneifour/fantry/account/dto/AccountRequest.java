package com.eneifour.fantry.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {
    private String accountName;
    private String bankName;
    private char isActive;
    private char isRefundable;
    private int memberId;
}
