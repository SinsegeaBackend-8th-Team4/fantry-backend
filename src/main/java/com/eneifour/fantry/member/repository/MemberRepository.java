package com.eneifour.fantry.member.repository;

import com.eneifour.fantry.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Integer> {
}
