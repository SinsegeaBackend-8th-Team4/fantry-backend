package com.eneifour.fantry.inquiry.repository;

import com.eneifour.fantry.inquiry.domain.InquiryAttachment;
import com.eneifour.fantry.inquiry.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryAttachmentRepository extends JpaRepository<InquiryAttachment, Integer>{
    List<InquiryAttachment> findByInquiry(Inquiry inquiry);
}