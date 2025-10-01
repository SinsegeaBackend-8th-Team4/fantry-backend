package com.eneifour.fantry.auction.repository;

import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.auction.domain.SaleStatus;
import com.eneifour.fantry.auction.domain.SaleType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction,Integer> {

    //판매 상품 중 , 특정 sale_type 으로 전체 조회
    List<Auction> findBySaleType(SaleType saleType);

    //판매 상품 중 , 특정 sale_status로 전체 조회
    List<Auction> findBySaleStatus(SaleStatus saleStatus);

    //판매 상품 중 , 특정 sale_type 이면서 특정 sale_status 인 모든 리스트 조회
    List<Auction> findBySaleTypeAndSaleStatus(SaleType saleType, SaleStatus saleStatus);

    //판매 상품 중 , member_id 를 기준으로 sale_status 가 특정 상품인 것 조회
    List<Auction> findByInventoryItem_Member_MemberIdAndSaleStatus(int memberId, SaleStatus saleStatus);

    //판매 상품 중 , member_id 기준으로 모든 상품 조회
    List<Auction> findByInventoryItem_Member_MemberId(int memberId);

    //판매 상품 중 , 경매가 끝난 상품을 status 와 datetime으로 조회
    List<Auction> findBySaleStatusAndEndTimeBefore(SaleStatus saleStatus, LocalDateTime endTime);

    //비관적 쓰기 락(PESSIMISTIC_WRITE)을 사용하여 Auction 엔티티를 조회
    //이 메서드를 호출한 트랜잭션이 커밋/롤백될 때까지 해당 auction_id의 행은 Lock
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Auction a where a.auctionId = :auctionId")
    Optional<Auction> findByIdForUpdate(int auctionId);

}
