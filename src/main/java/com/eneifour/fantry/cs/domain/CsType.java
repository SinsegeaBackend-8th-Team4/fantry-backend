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
}
