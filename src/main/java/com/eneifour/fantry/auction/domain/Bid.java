package com.eneifour.fantry.auction.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bid")
@Getter
@Setter
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
