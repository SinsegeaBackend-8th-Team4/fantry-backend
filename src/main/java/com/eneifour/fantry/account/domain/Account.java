package com.eneifour.fantry.account.domain;

import com.eneifour.fantry.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private int accountId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "create_at", insertable = false, updatable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private String createAt;

    @Column(name = "is_active")
    private char isActive;

    @Column(name = "is_refundable")
    private char isRefundable;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
