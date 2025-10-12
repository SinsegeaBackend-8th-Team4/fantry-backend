package com.eneifour.fantry.address.domain;

import com.eneifour.fantry.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    // 엔티티 업데이트 메서드 (더티 체킹용)
    public void update(String destinationAddress, String alias, String recipientName,
                       String recipientTel, char isDefault, Member member) {
        this.destinationAddress = destinationAddress;
        this.alias = alias;
        this.recipientName = recipientName;
        this.recipientTel = recipientTel;
        this.isDefault = isDefault;
        this.member = member;
    }

    //기본 배송지 선택
    public void setIsDefault(char isDefault) {
        this.isDefault = isDefault;
    }
}
