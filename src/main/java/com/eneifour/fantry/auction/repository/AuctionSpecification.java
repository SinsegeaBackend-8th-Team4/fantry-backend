package com.eneifour.fantry.auction.repository;

import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.auction.dto.AuctionSearchCondition;
import com.eneifour.fantry.common.util.AbstractSpecification;
import com.eneifour.fantry.catalog.domain.Artist;
import com.eneifour.fantry.inspection.domain.ProductInspection;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
                .and((root, query, criteriaBuilder) -> {
                    if (condition.getArtistGroupType() == null) {
                        return null;
                    }
                    Join<Auction, ProductInspection> productInspectionJoin = root.join("productInspection");
                    Subquery<Integer> artistIdSubquery = query.subquery(Integer.class);
                    Root<Artist> artistRoot = artistIdSubquery.from(Artist.class);
                    artistIdSubquery.select(artistRoot.get("artistId"))
                                    .where(criteriaBuilder.equal(artistRoot.get("groupType"), condition.getArtistGroupType()));
                    return criteriaBuilder.in(productInspectionJoin.get("artistId")).value(artistIdSubquery);
                })
                // 조건 DTO의 keyword 필드가 존재하면, 키워드 검색 조건을 추가
                .and((root, query, criteriaBuilder) -> {
                    if (!StringUtils.hasText(condition.getKeyword())) {
                        return null; // 키워드가 없으면 필터링하지 않음
                    }

                    String keywordPattern = "%" + condition.getKeyword() + "%";

                    // Auction -> ProductInspection 조인
                    Join<Auction, ProductInspection> productInspectionJoin = root.join("productInspection");

                    // 1. 상품명(itemName)에 대한 LIKE 조건 생성
                    Predicate itemNamePredicate = criteriaBuilder.like(productInspectionJoin.get("itemName"), keywordPattern);

                    // 2. 아티스트명(nameKo, nameEn)에 대한 서브쿼리 생성
                    // Artist 테이블에서 nameKo 또는 nameEn이 키워드를 포함하는 아티스트의 ID 목록을 조회
                    Subquery<Integer> artistIdSubquery = query.subquery(Integer.class);
                    Root<Artist> artistRoot = artistIdSubquery.from(Artist.class);
                    Predicate nameKoPredicate = criteriaBuilder.like(artistRoot.get("nameKo"), keywordPattern);
                    Predicate nameEnPredicate = criteriaBuilder.like(artistRoot.get("nameEn"), keywordPattern);
                    artistIdSubquery.select(artistRoot.get("artistId"))
                                    .where(criteriaBuilder.or(nameKoPredicate, nameEnPredicate));

                    // 3. ProductInspection의 artistId가 서브쿼리 결과에 포함되는지에 대한 조건 생성
                    Predicate artistIdPredicate = criteriaBuilder.in(productInspectionJoin.get("artistId")).value(artistIdSubquery);

                    // 4. 상품명 조건과 아티스트명 조건을 OR로 결합하여 최종 Predicate 반환
                    return criteriaBuilder.or(itemNamePredicate, artistIdPredicate);
                });

        return spec;
    }
}
