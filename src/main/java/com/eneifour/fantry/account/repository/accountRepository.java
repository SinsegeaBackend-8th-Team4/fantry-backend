package com.eneifour.fantry.account.repository;

import com.eneifour.fantry.account.domain.Account;
import com.eneifour.fantry.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface accountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByMember_MemberId(int memberId);
    Account findByAccountId(int accountId);
}
