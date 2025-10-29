package com.eneifour.fantry.member.repository;

import com.eneifour.fantry.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface MemberRepository extends JpaRepository<Member,Integer> {
    @Query("SELECT new map(" +
            "   count(m) as totalMembers, " +
            "   COALESCE(sum(case when FUNCTION('DATE', m.createAt) = CURRENT_DATE then 1 else 0 end), 0L) as todayRegisteredMembers) " +
            "FROM Member m")
    Map<String, Long> countMemberStats();
}
