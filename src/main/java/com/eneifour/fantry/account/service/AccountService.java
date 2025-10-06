package com.eneifour.fantry.account.service;

import com.eneifour.fantry.account.domain.Account;
import com.eneifour.fantry.account.dto.AccountRequest;
import com.eneifour.fantry.account.dto.AccountResponse;
import com.eneifour.fantry.account.exception.AccountErrorCode;
import com.eneifour.fantry.account.exception.AccountException;
import com.eneifour.fantry.account.repository.accountRepository;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.repository.JpaMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final accountRepository accountRepository;
    private final JpaMemberRepository memberRepository;

    //모든 계좌 가져오기
    public List<AccountResponse> getAccounts(){
        return AccountResponse.fromList(accountRepository.findAll());
    }

    //한명의 대한 모든 계좌 가져오기
    public List<AccountResponse> getAccountsByMember(int memberId){
        return AccountResponse.fromList(accountRepository.findByMember_MemberId(memberId));
    }

    //하나의 특정 계좌 정보 가져오기
    public AccountResponse getAccount(int accountId){
        Account account = accountRepository.findByAccountId(accountId);
        if(account == null){
            throw new AccountException(AccountErrorCode.ACCOUNT_NOT_FOUND);
        }
        return AccountResponse.from(account);
    }

    //계좌 추가하기
    @Transactional
    public void saveAccount(AccountRequest accountRequest){
        Member member = memberRepository.findByMemberId(accountRequest.getMemberId());  //이거 MemberRepository에 추가하기
        if(member == null){
            throw new AccountException(AccountErrorCode.ACCOUNT_NOT_FOUND_MEMBER);
        }

        Account account = Account.builder()
                .accountNumber(accountRequest.getAccountName())
                .bankName(accountRequest.getBankName())
                .isActive(accountRequest.getIsActive())
                .isRefundable(accountRequest.getIsRefundable())
                .member(member)
                .build();
        Account savedAccount = accountRepository.save(account);
        AccountResponse.from(savedAccount);
    }

    //계좌 수정하기
    @Transactional
    public void updateAccount(int accountId, AccountRequest accountRequest) throws AccountException{
        Account account = accountRepository.findByAccountId(accountId);
        if(account == null){
            throw new AccountException(AccountErrorCode.ACCOUNT_NOT_FOUND);
        }

        Member member = memberRepository.findByMemberId(accountRequest.getMemberId());  //이거 MemberRepository에 추가하기
        if(member == null){
            throw new AccountException(AccountErrorCode.ACCOUNT_NOT_FOUND_MEMBER);
        }

        account.update(
                accountRequest.getAccountName(),
                accountRequest.getBankName(),
                accountRequest.getIsActive(),
                accountRequest.getIsRefundable(),
                member
        );

        AccountResponse.from(account);
    }

    //계좌 삭제하기
    @Transactional
    public void deleteAccount(int accountId) throws AccountException{
        if(!accountRepository.existsById(accountId)){
            throw new AccountException(AccountErrorCode.ACCOUNT_NOT_FOUND);
        }
        accountRepository.deleteById(accountId);
    }
}
