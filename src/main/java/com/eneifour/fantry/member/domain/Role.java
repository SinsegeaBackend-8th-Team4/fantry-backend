package com.eneifour.fantry.member.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;

    @Column(name = "role_type")
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
}
