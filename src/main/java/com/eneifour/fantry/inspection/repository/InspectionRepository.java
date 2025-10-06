package com.eneifour.fantry.inspection.repository;

import com.eneifour.fantry.inspection.domain.InspectionStatus;
import com.eneifour.fantry.inspection.domain.ProductInspection;
import com.eneifour.fantry.inspection.dto.InspectionListDto;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionRepository extends JpaRepository<ProductInspection, Integer> {
    // 검수상태에 따른 검수 페이지 조회
    @Query(value = """
        select new com.eneifour.fantry.inspection.dto.InspectionListDto(
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
        where i.inspectionStatus = :status
        """)
    Page<InspectionListDto> findAllByInspectionStatus(@Param("status") InspectionStatus status, Pageable pageable);
}
