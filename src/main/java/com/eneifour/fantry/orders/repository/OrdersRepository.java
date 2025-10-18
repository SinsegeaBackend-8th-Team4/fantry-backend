package com.eneifour.fantry.orders.repository;

import com.eneifour.fantry.orders.domain.OrderStatus;
import com.eneifour.fantry.orders.domain.Orders;
import com.eneifour.fantry.payment.domain.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Integer> {

    Optional<Orders> findOrderDetailByOrdersId(@Param("ordersId") int ordersId);

    Page<Orders> findByMember_MemberIdAndOrderStatus(Pageable pageable, int memberId, OrderStatus orderStatus);
    Page<Orders> findByMember_MemberId(Pageable pageable, int memberId);
    Page<Orders> findByOrderStatus(Pageable pageable, OrderStatus orderStatus);
    Optional<Orders> findByPayment(Payment payment);


    Optional<Orders> findByAuction_AuctionId(int auctionId);
    List<Orders> findByMember_MemberId(int memberId);

    /**
     * 특정 상태와 업데이트 시간을 기준으로 주문을 조회합니다. (정산 대상 조회용)
     * @param orderStatus 조회할 주문 상태
     * @param cutoffDate 기준 시간
     * @return 주문 목록
     */
    List<Orders> findByOrderStatusAndUpdatedAtBefore(OrderStatus orderStatus, LocalDateTime cutoffDate);

    /**
     * 특정 상태와 생성 시간 범위를 기준으로 주문을 조회합니다.
     * @param orderStatus 조회할 주문 상태
     * @param startDate 시작 시간
     * @param endDate 종료 시간
     * @return 주문 목록
     */
    List<Orders> findByOrderStatusAndCreatedAtBetween(OrderStatus orderStatus, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT new map(" +
            "   count(o) as totalOrders, " +
            "   COALESCE(sum(case when o.orderStatus = com.eneifour.fantry.orders.domain.OrderStatus.PENDING_PAYMENT then 1 else 0 end), 0L) as pendingPaymentOrders, " +
            "   COALESCE(sum(case when o.orderStatus = com.eneifour.fantry.orders.domain.OrderStatus.PAID then 1 else 0 end), 0L) as paidOrders, " +
            "   COALESCE(sum(case when o.orderStatus = com.eneifour.fantry.orders.domain.OrderStatus.PREPARING_SHIPMENT then 1 else 0 end), 0L) as preparingShipmentOrders, " +
            "   COALESCE(sum(case when o.orderStatus = com.eneifour.fantry.orders.domain.OrderStatus.SHIPPED then 1 else 0 end), 0L) as shippedOrders, " +
            "   COALESCE(sum(case when o.orderStatus = com.eneifour.fantry.orders.domain.OrderStatus.DELIVERED then 1 else 0 end), 0L) as deliveredOrders, " +
            "   COALESCE(sum(case when o.orderStatus = com.eneifour.fantry.orders.domain.OrderStatus.CONFIRMED then 1 else 0 end), 0L) as confirmedOrders, " +
            "   COALESCE(sum(case when o.orderStatus = com.eneifour.fantry.orders.domain.OrderStatus.CANCEL_REQUESTED then 1 else 0 end), 0L) as cancelRequestedOrders, " +
            "   COALESCE(sum(case when o.orderStatus = com.eneifour.fantry.orders.domain.OrderStatus.CANCELLED then 1 else 0 end), 0L) as cancelledOrders, " +
            "   COALESCE(sum(case when o.orderStatus = com.eneifour.fantry.orders.domain.OrderStatus.REFUND_REQUESTED then 1 else 0 end), 0L) as refundRequestedOrders, " +
            "   COALESCE(sum(case when o.orderStatus = com.eneifour.fantry.orders.domain.OrderStatus.REFUNDED then 1 else 0 end), 0L) as refundedOrders) " +
            "FROM Orders o")
    Map<String, Long> countOrdersByStatus();

    long countByOrderStatus(OrderStatus orderStatus);

    @Query("SELECT SUM(o.payment.price) FROM Orders o WHERE o.orderStatus = :orderStatus")
    BigDecimal sumPriceByOrderStatus(@Param("orderStatus") OrderStatus orderStatus);
}
