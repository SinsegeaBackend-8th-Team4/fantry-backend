package com.eneifour.fantry.catalog.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "artist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer artistId;

    private String nameKo;
    private String nameEn;

    @Enumerated(EnumType.STRING)
    private GroupType groupType;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    private enum GroupType {
        MALE_GROUP, FEMALE_GROUP, MALE_SOLO, FEMALE_SOLO, MIXED, OTHER
    }
}
