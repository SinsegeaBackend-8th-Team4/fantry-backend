package com.eneifour.fantry.cs.repository;

import com.eneifour.fantry.cs.domain.Inquiry;
import com.eneifour.fantry.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Integer>, JpaSpecificationExecutor<Inquiry> {

    @EntityGraph(attributePaths = {"inquiredBy"})
    Page<Inquiry> findByInquiredByOrderByInquiredAtDesc(Member inquiredBy, Pageable pageable);

    /**
     * ID로 Inquiry를 조회할 때, N+1 문제를 방지하기 위해 연관된 엔티티들을 함께 fetch합니다.
     * JPQL대신 @EntityGraph 활용
     */
    @EntityGraph(attributePaths = {"inquiredBy", "answeredBy", "csType", "attachments", "attachments.filemeta"})
    @Query("select i from Inquiry i where i.inquiryId = :id")
    Optional<Inquiry> findWithAttachmentsById(@Param("id") int id);
}
