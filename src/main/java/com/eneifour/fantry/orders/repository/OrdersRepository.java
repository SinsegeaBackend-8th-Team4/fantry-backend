package com.eneifour.fantry.orders.repository;

import com.eneifour.fantry.orders.domain.OrderStatus;
import com.eneifour.fantry.orders.domain.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.eneifour.fantry.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Integer> {

    Optional<Orders> findOrderDetailByOrdersId(@Param("ordersId") int ordersId);

    Page<Orders> findByMember_MemberIdAndOrderStatus(Pageable pageable, int memberId, OrderStatus orderStatus);
    Page<Orders> findByMember_MemberId(Pageable pageable, int memberId);
    Page<Orders> findByOrderStatus(Pageable pageable, OrderStatus orderStatus);
    Optional<Orders> findByPayment(Payment payment);

    Optional<Orders> findByAuction_AuctionId(int auctionId);
}
