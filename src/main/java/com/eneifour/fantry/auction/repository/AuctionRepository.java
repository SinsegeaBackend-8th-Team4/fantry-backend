package com.eneifour.fantry.auction.repository;

import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.auction.domain.SaleStatus;
import com.eneifour.fantry.auction.domain.SaleType;
import com.eneifour.fantry.auction.dto.AuctionDetailResponse;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction,Integer> {

    /**
     * JPQL JOIN을 사용하여 경매 상세 정보를 DTO로 직접 조회합니다.
     * N+1 문제를 방지하고 단 한 번의 쿼리로 필요한 모든 데이터를 가져옵니다.
     * @param auctionId 조회할 경매의 ID
     * @return 경매 상세 정보가 담긴 AuctionDetailDTO
     */
    @Query("""
        SELECT NEW com.eneifour.fantry.auction.dto.AuctionDetailResponse(
            a.auctionId, pi.productInspectionId, pi.memberId, a.startPrice, CAST(a.saleStatus AS string), CAST(a.saleType AS string),
            a.startTime, a.endTime, pi.itemName, pi.itemDescription, pi.hashtags, gc.name, ar.nameKo, al.title
        )
        FROM Auction a
        JOIN a.productInspection pi
        LEFT JOIN GoodsCategory gc ON pi.goodsCategoryId = gc.goodsCategoryId
        LEFT JOIN Artist ar ON pi.artistId = ar.artistId
        LEFT JOIN Album al ON pi.albumId = al.albumId
        WHERE a.auctionId = :auctionId
    """)
    Optional<AuctionDetailResponse> findAuctionDetailById(@Param("auctionId") int auctionId);


    //판매 상품 중 , 특정 sale_type 으로 전체 조회
    Page<Auction> findBySaleType(SaleType saleType , Pageable pageable);

    //판매 상품 중 , 특정 sale_status로 전체 조회
    Page<Auction> findBySaleStatus(SaleStatus saleStatus , Pageable pageable);

    //판매 상품 중 , 특정 sale_type 이면서 특정 sale_status 인 모든 리스트 조회
    Page<Auction> findBySaleTypeAndSaleStatus(SaleType saleType, SaleStatus saleStatus , Pageable pageable);

    //판매 상품 중 , member_id 를 기준으로 sale_status 가 특정 상품인 것 조회
    List<Auction> findByProductInspection_MemberIdAndSaleStatus(int memberId, SaleStatus saleStatus);

    //판매 상품 중 , member_id 기준으로 모든 상품 조회
    List<Auction> findByProductInspection_MemberId(int memberId);

    /**
     * 특정 상태와 마감 시간 이전의 경매 목록을 페이징하여 조회합니다.
     * @param saleStatus 경매 상태
     * @param endTime 마감 시간 기준
     * @param pageable 페이징 정보
     * @return 페이징된 경매 목록 (Page<Auction>)
     */
    Page<Auction> findBySaleStatusAndEndTimeBefore(SaleStatus saleStatus, LocalDateTime endTime, Pageable pageable);

    //비관적 쓰기 락(PESSIMISTIC_WRITE)을 사용하여 Auction 엔티티를 조회
    //이 메서드를 호출한 트랜잭션이 커밋/롤백될 때까지 해당 auction_id의 행은 Lock
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Auction a where a.auctionId = :auctionId")
    Optional<Auction> findByIdForUpdate(int auctionId);

}
