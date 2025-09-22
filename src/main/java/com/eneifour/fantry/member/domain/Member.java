package com.eneifour.fantry.member.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private int memberId;

    @Column(unique = true)
    private String id;
    private String password;
    private String name;
    private String email;
    private String tel;
    private String sns;

    @Column(name = "create_at", insertable = false, updatable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private String createAt;

    @Column(name = "leaved_at")
    private String leavedAt;

    @Column(name = "is_active")
    private int isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
}