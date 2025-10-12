package com.eneifour.fantry.auction.domain;

import com.eneifour.fantry.auction.exception.AuctionException;
import com.eneifour.fantry.auction.exception.ErrorCode;
import com.eneifour.fantry.inspection.domain.ProductInspection;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Table(name = "auction")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auction_id")
    private int auctionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SaleType saleType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SaleStatus saleStatus;

    @Column(nullable = false)
    private int startPrice;

    @Column(nullable = true)
    private Integer finalPrice;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @CreationTimestamp // Entity가 생성될 때 시간이 자동 저장
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // Entity가 수정될 때 시간이 자동 저장
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_inspection_id")
    private ProductInspection productInspection;

    /**
     * 경매 및 판매를 '낙찰 완료 / 판매 완료' 상태로 변경.
     * //@param finalPrice 최종 낙찰가
     */
    public void closeAsSold(int finalPrice) {
        if (this.saleStatus != SaleStatus.ACTIVE) {
            throw new AuctionException(ErrorCode.AUCTION_NOT_ACTIVE);
        }
        this.saleStatus = SaleStatus.SOLD;
        this.finalPrice = finalPrice;
    }

    /**
     * 상품을 '유찰 / 미판매' 상태로 변경.
     */
    public void closeAsNotSold() {
        if (this.saleStatus != SaleStatus.ACTIVE) {
            throw new AuctionException(ErrorCode.AUCTION_NOT_ACTIVE);
        }
        this.saleStatus = SaleStatus.NOT_SOLD;
    }

    /*
    * 상품 status cancelld 로 변경
    *
    * */
    public void closeAsCancelled(){
        this.saleStatus = SaleStatus.CANCELLED;
    }

    /*
    * 상품 status를 'ACTIVE'로 변경
    * */
    public void activate(){
        this.saleStatus = SaleStatus.ACTIVE;
    }

    /*
     * 상품 판매 유형 변경
     * */
    public void changeSaleType(String saleType){
        this.saleType = SaleType.valueOf(saleType);
    }

}
