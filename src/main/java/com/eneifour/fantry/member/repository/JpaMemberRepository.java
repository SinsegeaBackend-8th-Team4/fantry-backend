package com.eneifour.fantry.member.repository;

import com.eneifour.fantry.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMemberRepository extends JpaRepository<Member, Integer> {
    //유저가 존재하는지 확인
    public Boolean existsById(String Id);

    //이메일로 아이디 찾기
    public Member findByEmail(String email);

    //아이디로 유저 한명 가져오기
    public Member findById(String id);

    //아이디로 유저 한명 삭제하기
    public void deleteById(String id);

}
