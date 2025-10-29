package com.eneifour.fantry.auction.domain;

import com.eneifour.fantry.auction.exception.AuctionException;
import com.eneifour.fantry.auction.exception.ErrorCode;
import com.eneifour.fantry.inspection.domain.ProductInspection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 상품(경매) 정보를 담는 도메인 클래스입니다.
 */
@Getter
@Table(name = "auction")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "상품(경매) 정보")
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auction_id")
    @Schema(description = "경매 ID (기본 키)")
    private int auctionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "판매 유형 (AUCTION, INSTANT_BUY)")
    private SaleType saleType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "판매 상태 (PREPARING, ACTIVE, SOLD, NOT_SOLD, CANCELLED)")
    private SaleStatus saleStatus;

    @Column(nullable = false)
    @Schema(description = "시작 가격")
    private int startPrice;

    @Column(nullable = true)
    @Schema(description = "최종 낙찰 가격")
    private Integer finalPrice;

    @Column(nullable = false)
    @Schema(description = "경매 시작 시간")
    private LocalDateTime startTime;

    @Column(nullable = false)
    @Schema(description = "경매 종료 시간")
    private LocalDateTime endTime;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @Schema(description = "생성 일시")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(insertable = false)
    @Schema(description = "수정 일시")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_inspection_id")
    @Schema(description = "연관된 상품 검수 정보")
    private ProductInspection productInspection;

    /**
     * 경매 및 판매를 '낙찰 완료 / 판매 완료' 상태로 변경합니다.
     * @param finalPrice 최종 낙찰가
     */
    public void closeAsSold(int finalPrice) {
        if (this.saleStatus != SaleStatus.ACTIVE && this.saleStatus != SaleStatus.REACTIVE) {
            throw new AuctionException(ErrorCode.AUCTION_NOT_ACTIVE);
        }
        this.saleStatus = SaleStatus.SOLD;
        this.finalPrice = finalPrice;
    }

    /**
     * 상품을 '유찰 / 미판매' 상태로 변경합니다.
     */
    public void closeAsNotSold() {
        if (this.saleStatus != SaleStatus.ACTIVE && this.saleStatus != SaleStatus.REACTIVE) {
            throw new AuctionException(ErrorCode.AUCTION_NOT_ACTIVE);
        }
        this.saleStatus = SaleStatus.NOT_SOLD;
    }

    /**
     * 상품 상태를 '취소'로 변경합니다.
     */
    public void closeAsCancelled(){
        this.saleStatus = SaleStatus.CANCELED;
    }

    /**
     * 상품 상태를 '활성'으로 변경합니다.
     */
    public void activate(){
        this.saleStatus = SaleStatus.ACTIVE;
    }

    /**
     * 상품 상태를 '재활성'으로 변경합니다.
     */
    public void reactivate(){
        this.saleStatus = SaleStatus.REACTIVE;
    }

    /**
     * 상품 판매 유형을 변경합니다.
     * @param saleType 새로운 판매 유형
     */
    public void changeSaleType(String saleType){
        this.saleType = SaleType.valueOf(saleType);
    }

}
