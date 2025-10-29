package com.eneifour.fantry.payment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ghost_payment",indexes = @Index(name = "idx_ghost_payment_status", columnList = "status"))
public class GhostPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ghost_payment_id")
    private Integer ghostPaymentId;
    @Column(name = "receipt_id", updatable = false)
    private String receiptId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private GhostPaymentStatus status;
}
