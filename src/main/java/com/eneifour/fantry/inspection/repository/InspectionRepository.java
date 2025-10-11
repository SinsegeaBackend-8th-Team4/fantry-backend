package com.eneifour.fantry.inspection.repository;

import com.eneifour.fantry.inspection.domain.InspectionStatus;
import com.eneifour.fantry.inspection.domain.ProductInspection;
import com.eneifour.fantry.inspection.dto.InspectionListResponse;
import com.eneifour.fantry.inspection.dto.OnlineInspectionDetailResponse;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InspectionRepository extends JpaRepository<ProductInspection, Integer> {
    // 검수상태에 따른 검수 페이지 조회
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
     * 검수 ID로 검수 상세 정보 조회 (파일 제외)
     * @param productInspectionId 검수 ID
     * @return 조회된 검수 상세 정보
     */
    @Query("""
        select new com.eneifour.fantry.inspection.dto.OnlineInspectionDetailResponse(
            i.productInspectionId, i.submissionUuid, i.inspectionStatus, i.submittedAt,
            i.itemName, i.itemDescription, i.hashtags,
            gc.name, a.nameKo, al.title,
            i.expectedPrice, i.marketAvgPrice, i.sellerHopePrice,
            new com.eneifour.fantry.inspection.dto.OnlineInspectionDetailResponse$UserInfo(m.name, m.email, m.tel),
            i.bankName, i.bankAccount,
            i.shippingAddress, i.shippingAddressDetail,
            i.templateId, i.templateVersion
        )
        from ProductInspection i
        join Member m on m.memberId = i.memberId
        join GoodsCategory gc on gc.goodsCategoryId = i.goodsCategoryId
        join Artist a on a.artistId = i.artistId
        left join Album al on al.albumId = i.albumId
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
}
