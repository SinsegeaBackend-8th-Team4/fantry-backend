package com.eneifour.fantry.auction.repository;

import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.auction.dto.AuctionSearchCondition;
import com.eneifour.fantry.common.util.AbstractSpecification;
import com.eneifour.fantry.catalog.domain.Artist; // Artist 엔티티 임포트
import com.eneifour.fantry.inspection.domain.ProductInspection; // ProductInspection 엔티티 임포트
import jakarta.persistence.criteria.Join; // Join 임포트
import jakarta.persistence.criteria.Root; // Root 임포트
import jakarta.persistence.criteria.Subquery; // Subquery 임포트
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * Auction 엔티티에 대한 동적 쿼리를 생성
 */
@Component
public class AuctionSpecification extends AbstractSpecification<Auction, AuctionSearchCondition> {

    /**
     * 검색 조건 DTO(AuctionSearchCondition)를 기반으로 최종 Specification 객체를 조립.
     * @param condition 클라이언트로부터 받은 검색 조건 객체(집합)
     * @return 조립된 최종 Specification 객체
     */
    @Override
    public Specification<Auction> toSpecification(AuctionSearchCondition condition) {
        // 기본 Specification (WHERE 1=1) 에서 시작
        Specification<Auction> spec = base()
                // 조건 DTO의 saleType 필드가 null이 아니면, saleType 검색 조건을 추가
                .and(equal("saleType", condition.getSaleType()))
                // 조건 DTO의 saleStatus 필드가 null이 아니면, saleStatus 검색 조건을 추가
                .and(equal("saleStatus", condition.getSaleStatus()))
                // 조건 DTO의 groupType 필드가 null이 아니면, groupType 검색 조건을 추가
                // ProductInspection 엔티티에 Artist 객체 필드가 아닌 artistId(int)만 있는 경우를 처리합니다.
                // Auction -> ProductInspection의 artistId를 통해 Artist의 groupType을 필터링합니다.
                .and((root, query, criteriaBuilder) -> {
                    if (condition.getArtistGroupType() == null) {
                        return null; // groupType 조건이 없으면 필터링하지 않습니다.
                    }

                    // 1. Auction 엔티티에서 ProductInspection 엔티티로 조인합니다.
                    Join<Auction, ProductInspection> productInspectionJoin = root.join("productInspection");

                    // 2. Artist 엔티티에서 주어진 groupType을 가진 artistId들을 찾는 서브쿼리를 생성합니다.
                    Subquery<Integer> artistIdSubquery = query.subquery(Integer.class);
                    Root<Artist> artistRoot = artistIdSubquery.from(Artist.class); // Artist 엔티티의 Root
                    artistIdSubquery.select(artistRoot.get("artistId")) // Artist의 artistId를 선택
                                    .where(criteriaBuilder.equal(artistRoot.get("groupType"), condition.getArtistGroupType())); // groupType으로 필터링

                    // 3. productInspection의 artistId가 서브쿼리 결과(groupType에 해당하는 artistId 목록)에 포함되는지 확인합니다.
                    return criteriaBuilder.in(productInspectionJoin.get("artistId")).value(artistIdSubquery);
                });

        return spec;
    }
}
