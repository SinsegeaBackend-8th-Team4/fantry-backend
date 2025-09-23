package com.eneifour.fantry.saleItem.repository;

import com.eneifour.fantry.saleItem.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Integer> {
    List<Orders> findByMember_MemberId(int memberId);
}
