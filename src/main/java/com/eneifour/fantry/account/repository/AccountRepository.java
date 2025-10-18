package com.eneifour.fantry.account.repository;

import com.eneifour.fantry.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByMember_MemberId(int memberId);
    Account findByAccountId(int accountId);
    // 활성화된 계좌(한 건) 조회 - isActive는 '1' 또는 '0'
    Account findByMember_MemberIdAndIsActive(int memberId, char isActive);

    @Query("SELECT new map(" +
            "   count(a) as totalAccounts, " +
            "   COALESCE(sum(case when a.isActive = '1' then 1 else 0 end), 0L) as activeAccounts, " +
            "   COALESCE(sum(case when a.isActive = '0' then 1 else 0 end), 0L) as inactiveAccounts, " +
            "   COALESCE(sum(case when a.isRefundable = '1' then 1 else 0 end), 0L) as refundableAccounts, " +
            "   COALESCE(sum(case when a.isRefundable = '0' then 1 else 0 end), 0L) as nonRefundableAccounts) " +
            "FROM Account a")
    Map<String, Long> countAccountsByStatus();
}
