package com.eneifour.fantry.payment.repository;

import com.eneifour.fantry.payment.domain.GhostPayment;
import com.eneifour.fantry.payment.domain.GhostPaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GhostPaymentRepository extends JpaRepository<GhostPayment, Integer> {
    @Query("SELECT gp FROM GhostPayment gp WHERE gp.status <> :status")
    List<GhostPayment> findByStatusNot(@Param("status") GhostPaymentStatus status);
}
