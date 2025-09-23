package com.eneifour.fantry.saleItem.repository;

import com.eneifour.fantry.saleItem.domain.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid,Integer> {

    // 2. bidder_id가 일치하는 내역 전체 조회
    List<Bid> findByBidderId(int bidderId);

    // 3. item_id가 일치하는 내역 전체 조회
    List<Bid> findByItemId(int itemId);

    // 4. bidder_id 와 item_id 가 일치하는 내역 전체 조회
    List<Bid> findByBidderIdAndItemId(int bidderId, int itemId);

}
