package com.eneifour.fantry.cs.repository;

import com.eneifour.fantry.cs.domain.CsAttachment;
import com.eneifour.fantry.cs.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CsAttachmentRepository extends JpaRepository<CsAttachment, Integer>{
    List<CsAttachment> findByInquiry(Inquiry inquiry);
}