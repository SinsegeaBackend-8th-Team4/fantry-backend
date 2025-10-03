package com.eneifour.fantry.address.domain;

import com.eneifour.fantry.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private int addressId;

    @Column(name = "destination_address")
    private String destinationAddress;

    private String alias;

    @Column(name = "recipient_name")
    private String recipientName;

    @Column(name = "recipient_tel")
    private String recipientTel;

    @Column(name = "is_default")
    private char isDefault;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;
}
