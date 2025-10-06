package com.eneifour.fantry.account.controller;

import com.eneifour.fantry.account.dto.AccountRequest;
import com.eneifour.fantry.account.dto.AccountResponse;
import com.eneifour.fantry.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;

    //모든 계좌 내역 가져오기
    @GetMapping("/")
    public ResponseEntity<?> getAccount(){
        List<AccountResponse> accounts = accountService.getAccounts();
        return ResponseEntity.ok().body(Map.of("accountList", accounts));
    }

    //한명의 회원에 대한 모든 계좌 정보 가져오기
    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getAccountsMember(@PathVariable int memberId){
        List<AccountResponse> accounts = accountService.getAccountsByMember(memberId);
        return ResponseEntity.ok().body(Map.of("accountList", accounts));
    }

    //하나의 계좌 정보 가져오기
    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccount(@PathVariable int accountId){
        AccountResponse account = accountService.getAccount(accountId);
        return ResponseEntity.ok().body(Map.of("account", account));
    }

    //계좌 추가하기
    @PostMapping("/")
    public ResponseEntity<?> createAccount(@RequestBody AccountRequest account){
        accountService.saveAccount(account);
        return ResponseEntity.ok().body(Map.of("result", "계좌가 성공적으로 등록됨"));
    }

    //계좌 수정하기
    @PutMapping("/{accountId}")
    public ResponseEntity<?> updateAccount(@PathVariable int accountId, @RequestBody AccountRequest account){
        accountService.updateAccount(accountId, account);
        return ResponseEntity.ok().body(Map.of("result", "계좌가 성공적으로 수정됨"));
    }

    //계좌 삭제하기
    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable int accountId){
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok().body(Map.of("result", "계좌가 성공적으로 삭제됨"));
    }
}
