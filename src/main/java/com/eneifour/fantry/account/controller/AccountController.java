package com.eneifour.fantry.account.controller;

import com.eneifour.fantry.account.domain.Account;
import com.eneifour.fantry.account.model.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {
    private final AccountService accountService;

    //모든 계좌 내역 가져오기
    @GetMapping("/account")
    public ResponseEntity<?> getAccount(){
        List<Account> accounts = accountService.getAccounts();
        return ResponseEntity.ok().body(Map.of("accountList", accounts));
    }

    //한명의 회원에 대한 모든 계좌 정보 가져오기
    @GetMapping("/account/member/{memberId}")
    public ResponseEntity<?> getAccountsMember(@PathVariable int memberId){
        List<Account> accounts = accountService.getAccountsByMember(memberId);
        return ResponseEntity.ok().body(Map.of("accountList", accounts));
    }

    //하나의 계좌 정보 가져오기
    @GetMapping("/account/{accountId}")
    public ResponseEntity<?> getAccount(@PathVariable int accountId){
        Account account = accountService.getAccount(accountId);
        return ResponseEntity.ok().body(Map.of("account", account));
    }

    //계좌 추가하기
    @PostMapping("/account")
    public ResponseEntity<?> createAccount(@RequestBody Account account){
        accountService.saveAccount(account);
        return ResponseEntity.ok().body(Map.of("result", "계좌가 성공적으로 등록됨"));
    }

    //계좌 수정하기
    @PutMapping("/account/{accountId}")
    public ResponseEntity<?> updateAccount(@PathVariable int accountId, @RequestBody Account account){
        accountService.updateAccount(account);
        return ResponseEntity.ok().body(Map.of("result", "계좌가 성공적으로 수정됨"));
    }

    //계좌 삭제하기
    @DeleteMapping("/account/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable int accountId){
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok().body(Map.of("result", "계좌가 성공적으로 삭제됨"));
    }
}
