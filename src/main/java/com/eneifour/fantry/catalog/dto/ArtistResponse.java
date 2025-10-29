package com.eneifour.fantry.catalog.dto;

import com.eneifour.fantry.catalog.domain.Artist;
import com.eneifour.fantry.catalog.domain.GroupType;

import java.time.LocalDateTime;

public record ArtistResponse(int artistId, String nameKo, String nameEn, GroupType groupType, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static ArtistResponse from(Artist a) {
        return new ArtistResponse(
                a.getArtistId(),
                a.getNameKo(),
                a.getNameEn(),
                a.getGroupType(),
                a.getStatus().getLabel(),
                a.getCreatedAt(),
                a.getUpdatedAt()
        );
    }
}
