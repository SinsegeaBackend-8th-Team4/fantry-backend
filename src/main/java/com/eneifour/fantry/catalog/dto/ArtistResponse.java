package com.eneifour.fantry.catalog.dto;

import com.eneifour.fantry.catalog.domain.Artist;

public record ArtistResponse(int artistId, String nameKo, String nameEn) {
    public static ArtistResponse from(Artist a) {
        return new ArtistResponse(
                a.getArtistId(),
                a.getNameKo(),
                a.getNameEn()
        );
    }
}
