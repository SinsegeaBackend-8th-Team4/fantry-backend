package com.eneifour.fantry.saleItem.repository;

import com.eneifour.fantry.saleItem.domain.Auction;
import com.eneifour.fantry.saleItem.domain.SaleStatus;
import com.eneifour.fantry.saleItem.domain.SaleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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

}
