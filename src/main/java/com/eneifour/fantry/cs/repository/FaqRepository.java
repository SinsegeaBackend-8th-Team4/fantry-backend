package com.eneifour.fantry.cs.repository;

import com.eneifour.fantry.cs.domain.Faq;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Integer>, JpaSpecificationExecutor<Faq> {

    @EntityGraph(attributePaths = {"createdBy", "updatedBy", "csType", "attachments", "attachments.filemeta"})
    @Query("select f from Faq f where f.faqId = :id")
    Optional<Faq> findWithAttachmentsById(@Param("id") int id);
}
