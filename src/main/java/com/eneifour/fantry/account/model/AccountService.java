package com.eneifour.fantry.account.model;

import com.eneifour.fantry.account.domain.Account;
import com.eneifour.fantry.account.exception.AccountErrorCode;
import com.eneifour.fantry.account.exception.AccountException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final JpaAccountRepository jpaAccountRepository;

    //모든 계좌 가져오기
    public List<Account> getAccounts(){
        return jpaAccountRepository.findAll();
    }

    //한명의 대한 모든 계좌 가져오기
    public List<Account> getAccountsByMember(int memberId){
        return jpaAccountRepository.findByMemberId(memberId);
    }

    //하나의 특정 계좌 정보 가져오기
    public Account getAccount(int accountId){
        return jpaAccountRepository.findByAccountId(accountId);
    }

    //계좌 추가하기
    public void saveAccount(Account account){
        jpaAccountRepository.save(account);
    }

    //계좌 수정하기
    @Transactional
    public void updateAccount(Account account) throws AccountException{
        if(!jpaAccountRepository.existsById(account.getAccountId())){
            throw new AccountException(AccountErrorCode.ACCOUNT_NOT_FOUND);
        }
        jpaAccountRepository.save(account);
    }

    //계좌 삭제하기
    @Transactional
    public void deleteAccount(int accountId) throws AccountException{
        if(!jpaAccountRepository.existsById(accountId)){
            throw new AccountException(AccountErrorCode.ACCOUNT_NOT_FOUND);
        }
        jpaAccountRepository.deleteById(accountId);
    }

}
