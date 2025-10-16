package com.eneifour.fantry.auction.dto;

import com.eneifour.fantry.auction.domain.SaleStatus;
import com.eneifour.fantry.auction.domain.SaleType;
import com.eneifour.fantry.catalog.domain.GroupType;
import lombok.Getter;
import lombok.Setter;

/**
 * 경매 목록 조회를 위한 동적 검색 조건을 담는 DTO입니다.
 */
@Getter
@Setter
public class AuctionSearchCondition {

    /**
     * 검색할 판매 유형 (AUCTION,INSTANT_BUY)
     */
    private SaleType saleType;

    /**
     * 검색할 판매 상태 (PREPARING, ACTIVE, SOLD_OUT, CANCELLED)
     */
    private SaleStatus saleStatus;

    /**
     * 검색할 그룹 유형 (MALE_SOLO, GIRL_GROUP)
     */
    private GroupType artistGroupType;
}
