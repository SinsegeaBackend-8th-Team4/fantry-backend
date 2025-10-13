package com.eneifour.fantry.cs.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="cs_type")
public class CsType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int csTypeId;

    @Column(nullable = false)
    private String name;
    // 1. 배송문의, 2. 결제문의 3. 기타문의 4. 상품문의 5. 환불/반품 문의, 6. 판매 문의
}
