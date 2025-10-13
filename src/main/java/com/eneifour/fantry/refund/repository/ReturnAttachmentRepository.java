package com.eneifour.fantry.refund.repository;

import com.eneifour.fantry.refund.domain.ReturnAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 환불/반품 요청의 첨부파일(ReturnAttachment) 엔티티에 대한 데이터베이스 작업을 처리하는 리포지토리입니다.
 */
public interface ReturnAttachmentRepository extends JpaRepository<ReturnAttachment, Integer> {
}
