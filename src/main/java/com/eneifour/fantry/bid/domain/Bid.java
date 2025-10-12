package com.eneifour.fantry.bid.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bid")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_id")
    private int bidId;

    @Column(nullable = false, updatable = false)
    private int bidAmount;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime bidAt;

    private String bidderName;

    private int bidderId;

    private String itemName;

    private int itemId;

}
