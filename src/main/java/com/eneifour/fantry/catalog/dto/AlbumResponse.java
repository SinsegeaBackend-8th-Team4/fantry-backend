package com.eneifour.fantry.catalog.dto;

import com.eneifour.fantry.catalog.domain.Album;

import java.time.LocalDate;

public record AlbumResponse(int albumId, ArtistResponse artist, String title, LocalDate releaseDate, String version) {
    public static AlbumResponse from(Album a) {
        return new AlbumResponse(
                a.getAlbumId(),
                ArtistResponse.from(a.getArtist()),
                a.getTitle(),
                a.getReleaseDate(),
                a.getVersion()
        );
    }
}