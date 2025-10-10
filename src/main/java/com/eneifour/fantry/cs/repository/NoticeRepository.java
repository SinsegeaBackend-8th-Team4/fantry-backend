package com.eneifour.fantry.cs.repository;

import com.eneifour.fantry.cs.domain.Notice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer>, JpaSpecificationExecutor<Notice> {

    /**
     * ID로 Notice를 조회할 때, N+1 문제를 방지하기 위해 연관된 엔티티들을 함께 fetch합니다.
     * @param id 조회할 공지사항 ID
     * @return 연관 엔티티가 모두 채워진 Optional<Notice> 객체
     */
    @EntityGraph(attributePaths = {"createdBy", "updatedBy", "csType", "attachments", "attachments.filemeta"})
    @Query("select n from Notice n where n.noticeId = :id")
    Optional<Notice> findWithAttachmentsById(@Param("id") int id);
}