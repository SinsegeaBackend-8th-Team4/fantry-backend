package com.eneifour.fantry.saleItem.repository;

import com.eneifour.fantry.saleItem.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member,Integer>{
}
