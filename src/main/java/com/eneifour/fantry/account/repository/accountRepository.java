package com.eneifour.fantry.account.repository;

import com.eneifour.fantry.account.domain.Account;
import com.eneifour.fantry.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface accountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByMember_MemberId(int memberId);
    Account findByAccountId(int accountId);
    // 활성화된 계좌(한 건) 조회 - isActive는 '1' 또는 '0'
    Account findByMember_MemberIdAndIsActive(int memberId, char isActive);
}
