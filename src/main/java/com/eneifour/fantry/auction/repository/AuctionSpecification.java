package com.eneifour.fantry.auction.repository;

import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.auction.dto.AuctionSearchCondition;
import com.eneifour.fantry.common.util.AbstractSpecification;
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
                .and(equal("groupType", condition.getGroupType()));

        return spec;
    }
}
