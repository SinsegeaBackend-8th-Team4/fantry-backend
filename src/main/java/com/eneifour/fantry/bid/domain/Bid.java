package com.eneifour.fantry.bid.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 입찰 정보를 담는 도메인 클래스입니다.
 */
@Entity
@Table(name = "bid")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "입찰 정보")
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_id")
    @Schema(description = "입찰 ID (기본 키)")
    private int bidId;

    @Column(nullable = false, updatable = false)
    @Schema(description = "입찰 금액")
    private int bidAmount;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @Schema(description = "입찰 시간")
    private LocalDateTime bidAt;

    @Schema(description = "입찰자 이름")
    private String bidderName;

    @Schema(description = "입찰자 ID")
    private int bidderId;

    @Schema(description = "상품 이름")
    private String itemName;

    @Schema(description = "상품 ID")
    private int itemId;

}
