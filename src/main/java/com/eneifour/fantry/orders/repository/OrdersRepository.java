package com.eneifour.fantry.orders.repository;

import com.eneifour.fantry.orders.domain.Orders;
import com.eneifour.fantry.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Integer> {
    List<Orders> findByMember_MemberId(int memberId);
    Optional<Orders> findByPayment(Payment payment);
}
