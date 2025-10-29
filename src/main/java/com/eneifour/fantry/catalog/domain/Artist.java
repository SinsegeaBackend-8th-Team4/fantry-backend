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
    private int artistId;

    private String nameKo;
    private String nameEn;

    @Enumerated(EnumType.STRING)
    private GroupType groupType;

    @Enumerated(EnumType.STRING)
    private ArtistStatus status = ArtistStatus.PENDING;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}