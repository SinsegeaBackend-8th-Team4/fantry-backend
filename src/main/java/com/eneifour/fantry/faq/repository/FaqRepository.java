package com.eneifour.fantry.faq.repository;

import com.eneifour.fantry.faq.domain.Faq;
import com.eneifour.fantry.inquiry.domain.CsStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Integer>, JpaSpecificationExecutor<Faq> {

    @EntityGraph(attributePaths = {"createdBy", "updatedBy", "csType", "attachments", "attachments.filemeta"})
    @Query("select f from Faq f where f.faqId = :id")
    Optional<Faq> findWithAttachmentsById(@Param("id") int id);

    long countByStatus(CsStatus status);

    @Query("SELECT new map(" +
            "   count(f) as totalFaqs, " +
            "   COALESCE(sum(case when f.status = com.eneifour.fantry.inquiry.domain.CsStatus.DRAFT then 1 else 0 end), 0L) as draftFaqs, " +
            "   COALESCE(sum(case when f.status = com.eneifour.fantry.inquiry.domain.CsStatus.ACTIVE then 1 else 0 end), 0L) as activeFaqs, " +
            "   COALESCE(sum(case when f.status = com.eneifour.fantry.inquiry.domain.CsStatus.PINNED then 1 else 0 end), 0L) as pinnedFaqs, " +
            "   COALESCE(sum(case when f.status = com.eneifour.fantry.inquiry.domain.CsStatus.INACTIVE then 1 else 0 end), 0L) as inactiveFaqs) " +
            "FROM Faq f")
    Map<String, Long> countFaqsByStatus();
}
