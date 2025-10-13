package com.eneifour.fantry.refund.repository;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.orders.domain.Orders;
import com.eneifour.fantry.refund.domain.ReturnRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * 환불/반품 요청(ReturnRequest) 엔티티에 대한 데이터베이스 작업을 처리하는 리포지토리입니다.
 * <p>JpaSpecificationExecutor를 상속받아 동적인 쿼리 생성을 지원합니다.
 */
public interface ReturnRepository extends JpaRepository<ReturnRequest, Integer>, JpaSpecificationExecutor<ReturnRequest> {

    /**
     * 특정 사용자의 환불/반품 요청 목록을 페이징하여 조회합니다.
     * <p>N+1 문제를 방지하기 위해 @EntityGraph를 사용하여 주문, 회원 정보를 함께 조회합니다.
     */
    @EntityGraph(attributePaths = {"orders", "member"})
    Page<ReturnRequest> findByMember(Member member, Pageable pageable);

    /**
     * 특정 주문에 대해 이미 환불/반품 요청이 존재하는지 확인합니다.
     */
    boolean existsByOrders(Orders orders);

    /**
     * ID를 기준으로 환불/반품 요청을 조회하되, 관련된 모든 연관 엔티티(첨부파일, 상태 이력 등)를 함께 조회합니다.
     * <p>상세 조회 시 발생하는 N+1 문제를 해결하기 위한 커스텀 쿼리입니다.
     */
    @EntityGraph(attributePaths = {"orders", "member", "orders.payment", "attachments.filemeta", "statusHistories.updatedBy"})
    @Query("select r from ReturnRequest r where r.returnRequestId = :id")
    Optional<ReturnRequest> findWithAttachmentsAndHistoriesById(@Param("id") int id);
}
