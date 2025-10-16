package com.eneifour.fantry.inspection.repository;

import com.eneifour.fantry.auction.dto.AuctionDetailResponse;
import com.eneifour.fantry.catalog.domain.GroupType;
import com.eneifour.fantry.inspection.domain.InspectionStatus;
import com.eneifour.fantry.inspection.domain.InventoryStatus;
import com.eneifour.fantry.inspection.domain.ProductInspection;
import com.eneifour.fantry.inspection.dto.InspectionListResponse;
import com.eneifour.fantry.inspection.dto.InventoryListResponse;
import com.eneifour.fantry.inspection.dto.MyInspectionResponse;
import com.eneifour.fantry.inspection.dto.OnlineInspectionDetailResponse;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface InspectionRepository extends JpaRepository<ProductInspection, Integer> {
    /**
     * 카테고리, 아티스트, 앨범(선택)을 기준으로 완료된 검수 상품의 평균 매입가 조회
     * @param goodsCategoryId 카테고리 ID
     * @param artistId 아티스트 ID
     * @param albumId 앨범 ID (null 가능)
     * @param completed 검수상태 COMPLETED
     * @return 평균가
     */
    @Query("""
        select AVG(i.finalBuyPrice)
        from ProductInspection i
        where i.inspectionStatus = :completed
        and i.goodsCategoryId = :goodsCategoryId
        and i.artistId = :artistId
        and (:albumId IS NULL OR i.albumId = :albumId)
    """)
    Optional<BigDecimal> getMarketAvgPrice(
        @Param("goodsCategoryId") int goodsCategoryId, @Param("artistId")int artistId, @Param("albumId") Integer albumId, @Param("completed") InspectionStatus completed
    );


    /**
     * 시세 조회에 사용된 데이터 건수 조회
     *
     * @param goodsCategoryId 카테고리 ID
     * @param artistId        아티스트 ID
     * @param albumId         앨범 ID (null 가능)
     * @param completed       검수상태 COMPLETED
     * @return 데이터 건수
     */
    @Query("""
    select count(*)
    from ProductInspection i
    where i.inspectionStatus = :completed
    and i.goodsCategoryId = :goodsCategoryId
    and i.artistId = :artistId
    and (:albumId IS NULL OR i.albumId = :albumId)
    """)
    int countForMarketPrice(
        @Param("goodsCategoryId") int goodsCategoryId, @Param("artistId")int artistId, @Param("albumId") Integer albumId, @Param("completed") InspectionStatus completed
    );

    /**
     * 검수 상태로 검수 리스트 조회
     * @param statuses 검수 상태 리스트
     * @param pageable 페이지 정보
     * @return 검수 리스트
     */
    @Query(value = """
        select new com.eneifour.fantry.inspection.dto.InspectionListResponse(
            i.productInspectionId,
            i.submissionUuid,
            i.memberId, m.name,
            i.goodsCategoryId, gc.name,
            i.artistId, a.nameKo,
            i.albumId, al.title,
            i.itemName,
            i.expectedPrice,
            i.marketAvgPrice,
            i.sellerHopePrice,
            i.submittedAt,
            i.inspectionStatus
        )
        from ProductInspection i
        join Member m on m.memberId = i.memberId
        join GoodsCategory gc on gc.goodsCategoryId = i.goodsCategoryId
        join Artist a on a.artistId = i.artistId
        left join Album al on al.albumId = i.albumId
        where i.inspectionStatus IN :statuses
        """)
    Page<InspectionListResponse> findAllByInspectionStatusIn(@Param("statuses") List<InspectionStatus> statuses, Pageable pageable);

    /**
     * 재고 상태로 리스트 조회
     * @param inventoryStatuses 재고 상태 리스트
     * @param pageable 페이지 정보
     * @return 재고 리스트
     */
    @Query(value = """
        select new com.eneifour.fantry.inspection.dto.InventoryListResponse(
            i.productInspectionId,
            i.memberId, m.name,
            i.goodsCategoryId, gc.name,
            i.artistId, a.nameKo,
            i.albumId, al.title,
            i.itemName,
            i.expectedPrice,
            i.marketAvgPrice,
            i.sellerHopePrice,
            i.submittedAt,
            i.inspectionStatus,
            i.inventoryStatus
        )
        from ProductInspection i
        join Member m on m.memberId = i.memberId
        join GoodsCategory gc on gc.goodsCategoryId = i.goodsCategoryId
        join Artist a on a.artistId = i.artistId
        left join Album al on al.albumId = i.albumId
        where i.inspectionStatus = 'COMPLETED'
        and i.inventoryStatus in :inventoryStatuses
        """)
    Page<InventoryListResponse> findAllByInventoryStatusIn(
            @Param("inventoryStatuses") List<InventoryStatus> inventoryStatuses,
            Pageable pageable);

    /**
     * 검수 ID로 검수 상세 정보 조회 (파일 제외)
     * @param productInspectionId 검수 ID
     * @return 조회된 검수 상세 정보
     */
    @Query("""
        select new com.eneifour.fantry.inspection.dto.OnlineInspectionDetailResponse(
            i.productInspectionId, i.submissionUuid, i.inspectionStatus, i.submittedAt,
            i.itemName, i.itemDescription, i.hashtags,
            i.goodsCategoryId, gc.name,i.artistId, a.nameKo, i.albumId, al.title,
            i.expectedPrice, i.marketAvgPrice, i.sellerHopePrice,
            new com.eneifour.fantry.inspection.dto.OnlineInspectionDetailResponse$UserInfo(m.memberId, m.name, m.email, m.tel),
            i.bankName, i.bankAccount,
            i.shippingAddress, i.shippingAddressDetail,
            i.templateId, i.templateVersion,
            new com.eneifour.fantry.inspection.dto.OnlineInspectionDetailResponse$UserInfo(insp.memberId, insp.name, insp.email, insp.tel),
            i.firstRejectionReason
        )
        from ProductInspection i
        join Member m on m.memberId = i.memberId
        join GoodsCategory gc on gc.goodsCategoryId = i.goodsCategoryId
        join Artist a on a.artistId = i.artistId
        left join Album al on al.albumId = i.albumId
        left join Member insp on insp.memberId = i.firstInspectorId
        where i.productInspectionId = :productInspectionId
        """)
    Optional<OnlineInspectionDetailResponse> findInspectionDetailById(@Param("productInspectionId") int productInspectionId);

    /**
     * 검수 ID로 검수 파일 정보 목록 조회
     * @param productInspectionId 검수 ID
     * @return 조회된 파일 정보 DTO 리스트
     */
    @Query("""
        select new com.eneifour.fantry.inspection.dto.OnlineInspectionDetailResponse$FileInfo(
            f.inspectionFileId,
            fm.storedFilePath,
            fm.fileType
        )
        from InspectionFile f
        join f.fileMeta fm
        where f.productInspection.productInspectionId = :productInspectionId
        """)
    List<OnlineInspectionDetailResponse.FileInfo> findFilesById(@Param("productInspectionId") int productInspectionId);

    /**
     * 검수 ID로 검수 파일 정보 목록 조회 (Auction DTO 용)
     * @param productInspectionId 검수 ID
     * @return 조회된 파일 정보 DTO 리스트
     */
    @Query("""
        select new com.eneifour.fantry.auction.dto.AuctionDetailResponse$FileInfo(
            f.inspectionFileId,
            fm.storedFilePath,
            fm.fileType
        )
        from InspectionFile f
        join f.fileMeta fm
        where f.productInspection.productInspectionId = :productInspectionId
        """)
    List<AuctionDetailResponse.FileInfo> findFilesByProductInspectionId(@Param("productInspectionId") int productInspectionId);

    /**
     * 특정 회원 검수 신청 목록 조회
     *
     * @param memberId 회원 ID
     * @param pageable 페이지 정보
     * @return 해당 회원 검수 현황 리스트
     */
    @Query("""
        SELECT new com.eneifour.fantry.inspection.dto.MyInspectionResponse(
            i.productInspectionId, i.itemName, i.itemDescription,
            gc.name, a.nameKo, i.sellerHopePrice, i.finalBuyPrice,
            i.inspectionStatus, i.submittedAt, i.firstRejectionReason,
            i.secondRejectionReason, i.priceDeductionReason, i.onlineInspectedAt,
            i.offlineInspectedAt, i.completedAt
        )
        FROM ProductInspection i
        JOIN GoodsCategory gc ON gc.goodsCategoryId = i.goodsCategoryId
        JOIN Artist a ON a.artistId = i.artistId
        WHERE i.memberId = :memberId
        ORDER BY i.submittedAt DESC
    """)
    Page<MyInspectionResponse> findMyInspectionsByMemberId(@Param("memberId") int memberId, Pageable pageable);

    /**
     * 검수 ID로 등록한 아티스트 그룹 조회
     * @param inspectionId 검수 ID
     * @return 아티스트 그룹(ENUM)
     */
    @Query("""
        SELECT a.groupType
        FROM ProductInspection i
        JOIN Artist a ON a.artistId = i.artistId
        WHERE i.productInspectionId = :inspectionId
    """)
    Optional<GroupType> findGroupTypeById(@Param("inspectionId") int inspectionId);

}
