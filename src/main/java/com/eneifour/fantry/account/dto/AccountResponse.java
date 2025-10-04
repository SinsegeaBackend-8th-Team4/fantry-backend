package com.eneifour.fantry.account.dto;

import com.eneifour.fantry.account.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {
    private int accountId;
    private String accountName;
    private String bankName;
    private String createAt;
    private char isActive;
    private char isRefundable;
    private int memberId;

    public static AccountResponse from(Account account) {
        return AccountResponse.builder()
                .accountId(account.getAccountId())
                .accountName(account.getAccountNumber())
                .bankName(account.getBankName())
                .createAt(account.getCreateAt())
                .isActive(account.getIsActive())
                .isRefundable(account.getIsRefundable())
                .memberId(account.getMember().getMemberId())
                .build();
    }

    public static List<AccountResponse> fromList(List<Account> accounts) {
        return accounts.stream()
                .map(AccountResponse::from)
                .collect(Collectors.toList());
    }
}
