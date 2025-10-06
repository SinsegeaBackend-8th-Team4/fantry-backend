package com.eneifour.fantry.cs.repository;

import com.eneifour.fantry.cs.domain.Inquiry;
import com.eneifour.fantry.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Integer>, JpaSpecificationExecutor<Inquiry> {
    /**
     * 특정 사용자가 작성한 문의 목록을 최신순으로 페이징하여 조회.
     * @param inquiredBy 조회할 사용자(Member)
     * @param pageable 페이징 정보
     * @return 페이징된 문의 목록
     */
    @EntityGraph(attributePaths = {"inquiredBy"})
    Page<Inquiry> findByInquiredByOrderByInquiredAtDesc(Member inquiredBy, Pageable pageable);
}