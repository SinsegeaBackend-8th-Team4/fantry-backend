package com.eneifour.fantry.bid.repository;

import com.eneifour.fantry.bid.domain.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid,Integer> {

    // bidder_id가 일치하는 내역 전체 조회
    List<Bid> findByBidderId(int bidderId);

    // item_id가 일치하는 내역 전체 조회
    List<Bid> findByItemIdOrderByBidAmountDesc(int itemId);

    // bidder_id 와 item_id 가 일치하는 내역 전체 조회
    List<Bid> findByBidderIdAndItemId(int bidderId, int itemId);

    // auction_id 기준 , 가장 높은 금액 입찰기록 조회
    // 입찰기록이 없을 수 있으므로 , Null을 방지 하기 위해 Optional 로 반환
    Optional<Bid> findTopByItemIdOrderByBidAmountDesc(int itemId);

    @Query(value = "SELECT " +
            "    (SELECT COUNT(*) FROM bid) AS totalBids, " +
            "    (SELECT COUNT(*) FROM bid b JOIN auction a ON b.item_id = a.auction_id WHERE a.sale_status = 'ACTIVE') AS bidsOnActiveAuctions, " +
            "    (SELECT COUNT(*) FROM bid WHERE DATE(bid_at) = CURDATE()) AS bidsToday",
            nativeQuery = true)
    Map<String, Object> getBidStats();
}
