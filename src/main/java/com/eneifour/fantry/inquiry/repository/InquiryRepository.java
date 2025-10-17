package com.eneifour.fantry.inquiry.repository;

import com.eneifour.fantry.inquiry.domain.Inquiry;
import com.eneifour.fantry.inquiry.domain.InquiryStatus;
import com.eneifour.fantry.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;


@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Integer>, JpaSpecificationExecutor<Inquiry> {

    @EntityGraph(attributePaths = {"inquiredBy", "csType"})
    Page<Inquiry> findByInquiredByOrderByInquiredAtDesc(Member inquiredBy, Pageable pageable);

    /**
     * ID로 Inquiry를 조회할 때, N+1 문제를 방지하기 위해 연관된 엔티티들을 함께 fetch합니다.
     * JPQL대신 @EntityGraph 활용
     */
    @EntityGraph(attributePaths = {"inquiredBy", "answeredBy", "csType", "attachments", "attachments.filemeta"})
    @Query("select i from Inquiry i where i.inquiryId = :id")
    Optional<Inquiry> findWithAttachmentsById(@Param("id") int id);

    /**
     * (대시보드용) 특정 상태(status)를 가진 문의의 총 개수를 조회한다.
     * @param status 조회할 문의의 상태 (e.g., PENDING, IN_PROGRESS)
     * @return 해당 상태의 문의 건수 (long)
     */
    long countByStatus(InquiryStatus status);

    /**
     * (대시보드용) 특정 기간 사이에 생성된 문의의 총 개수를 조회한다.
     * '오늘 접수된 문의' 등을 계산하는 데 사용된다.
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 해당 기간에 생성된 문의 건수 (long)
     */
    long countByInquiredAtBetween(LocalDateTime start, LocalDateTime end);
}
