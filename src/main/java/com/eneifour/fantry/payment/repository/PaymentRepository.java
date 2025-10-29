package com.eneifour.fantry.payment.repository;

import com.eneifour.fantry.payment.domain.Payment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    @Query("SELECT p FROM Payment p ORDER BY COALESCE(p.cancelledAt, p.purchasedAt) DESC")
    Page<Payment> findAllDesc(Pageable pageable);

    @Query("SELECT p FROM Payment p ORDER BY COALESCE(p.cancelledAt, p.purchasedAt) ASC")
    Page<Payment> findAllAsc(Pageable pageable);

    Optional<Payment> findByPaymentId(Integer paymentId);

    Optional<Payment> findByReceiptId(String receiptId);

    Optional<Payment> findByOrderId(String orderId);

    void removeByOrderId(String orderId);

//    @Query("SELECT p.receiptUrl FROM Payment p WHERE p.paymentId = :paymentId")
//    String findReceiptUrlByPaymentId(@Param("paymentId") Integer paymentId);
}
