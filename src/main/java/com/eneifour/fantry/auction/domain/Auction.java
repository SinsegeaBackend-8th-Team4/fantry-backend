package com.eneifour.fantry.auction.domain;

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

    @Enumerated(EnumType.ORDINAL) // 0, 1 값으로 저장하기 위해 ORDINAL 사용
    @Column(nullable = false, columnDefinition = "TINYINT")
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
     * 경매를 '낙찰 완료' 상태로 변경합니다.
     * 이 메서드는 객체 스스로 상태 변경의 유효성을 검사.
     * //@param finalPrice 최종 낙찰가
     */
    public void closeAsSold(int finalPrice) {
        if (this.saleStatus != SaleStatus.ACTIVE) {
            throw new IllegalStateException("이미 마감 처리된 경매입니다. 현재 상태: " + this.saleStatus);
        }
        this.saleStatus = SaleStatus.SOLD;
        this.finalPrice = finalPrice;
    }

    /**
     * 경매를 '유찰' 상태로 변경.
     */
    public void closeAsNotSold() {
        if (this.saleStatus != SaleStatus.ACTIVE) {
            throw new IllegalStateException("이미 마감 처리된 경매입니다. 현재 상태: " + this.saleStatus);
        }
        this.saleStatus = SaleStatus.NOT_SOLD;
    }


}
