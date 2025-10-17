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
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // JpaSpecificationExecutor 임포트 추가
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction,Integer>, JpaSpecificationExecutor<Auction> { // JpaSpecificationExecutor 추가

    @Query("""
        SELECT NEW com.eneifour.fantry.auction.dto.AuctionDetailResponse(
            a.auctionId, pi.productInspectionId, pi.memberId, a.startPrice, CAST(a.saleStatus AS string), CAST(a.saleType AS string),
            a.startTime, a.endTime, pi.itemName, pi.itemDescription, pi.hashtags, gc.name, ar.nameKo, CAST(ar.groupType AS string) , al.title
        )
        FROM Auction a
        JOIN a.productInspection pi
        LEFT JOIN GoodsCategory gc ON pi.goodsCategoryId = gc.goodsCategoryId
        LEFT JOIN Artist ar ON pi.artistId = ar.artistId
        LEFT JOIN Album al ON pi.albumId = al.albumId
        WHERE a.auctionId = :auctionId
    """)
    Optional<AuctionDetailResponse> findByAuctionId(@Param("auctionId") int auctionId);

    @Query("""
        SELECT NEW com.eneifour.fantry.auction.dto.AuctionDetailResponse(
            a.auctionId, pi.productInspectionId, pi.memberId, a.startPrice, CAST(a.saleStatus AS string), CAST(a.saleType AS string),
            a.startTime, a.endTime, pi.itemName, pi.itemDescription, pi.hashtags, gc.name, ar.nameKo, CAST(ar.groupType AS string) , al.title
        )
        FROM Auction a
        JOIN a.productInspection pi
        LEFT JOIN GoodsCategory gc ON pi.goodsCategoryId = gc.goodsCategoryId
        LEFT JOIN Artist ar ON pi.artistId = ar.artistId
        LEFT JOIN Album al ON pi.albumId = al.albumId
        WHERE pi.productInspectionId = :productInspectionId
        ORDER BY a.createdAt DESC
    """)
    List<AuctionDetailResponse> findAuctionDetailsByProductInspectionIdOrderByCreatedAtDesc(@Param("productInspectionId") int productInspectionId);

    @Query("""
        SELECT a.auctionId, gc.name
        FROM Auction a
        JOIN a.productInspection pi ON a.productInspection.productInspectionId = pi.productInspectionId
        LEFT JOIN GoodsCategory gc ON pi.goodsCategoryId = gc.goodsCategoryId
        WHERE a.auctionId IN :auctionIds
    """)
    List<Object[]> findCategoryNamesByAuctionIds(@Param("auctionIds") List<Integer> auctionIds);


    Page<Auction> findBySaleType(SaleType saleType , Pageable pageable);

    Page<Auction> findBySaleStatus(SaleStatus saleStatus , Pageable pageable);

    Page<Auction> findBySaleTypeAndSaleStatus(SaleType saleType, SaleStatus saleStatus , Pageable pageable);

    List<Auction> findByProductInspection_MemberIdAndSaleStatus(int memberId, SaleStatus saleStatus);

    List<Auction> findByProductInspection_MemberId(int memberId);

    @Query("""
    SELECT DISTINCT a.auctionId
    FROM Auction a
    JOIN Bid b ON a.auctionId = b.itemId
    WHERE b.bidderId = :memberId
      AND a.saleStatus = 'ACTIVE'
""")
    List<Integer> findActiveAuctionsBidByMember(@Param("memberId") int memberId);

    Page<Auction> findBySaleStatusInAndEndTimeBefore(List<SaleStatus> saleStatuses, LocalDateTime endTime, Pageable pageable);

    /**
     * 특정 상태와 시작 시간 이전의 경매 목록을 페이징하여 조회합니다.
     * @param saleStatuses 경매 상태
     * @param startTime 시작 시간 기준
     * @param pageable 페이징 정보
     * @return 페이징된 경매 목록 (Page<Auction>)
     */
    Page<Auction> findBySaleStatusInAndStartTimeBefore(List<SaleStatus> saleStatuses, LocalDateTime startTime, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Auction a where a.auctionId = :auctionId")
    Optional<Auction> findByIdForUpdate(int auctionId);

}
