package com.eneifour.fantry.catalog.dto;

import com.eneifour.fantry.catalog.domain.Artist;

public record ArtistDto(int artistId, String nameKo, String nameEn) {
    public static ArtistDto from(Artist a) {
        return new ArtistDto(
                a.getArtistId(),
                a.getNameKo(),
                a.getNameEn()
        );
    }
}
