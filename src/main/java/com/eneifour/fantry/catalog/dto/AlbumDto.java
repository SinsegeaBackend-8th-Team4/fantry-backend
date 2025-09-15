package com.eneifour.fantry.catalog.dto;

import com.eneifour.fantry.catalog.domain.Album;

import java.time.LocalDate;

public record AlbumDto(Long albumId, Long artistId, String title, LocalDate releaseDate, String version) {
    public static AlbumDto from(Album a) {
        return new AlbumDto(
            a.getAlbumId(),
            a.getArtist().getArtistId(),
            a.getTitle(),
            a.getReleaseDate(),
            a.getVersion()
        );
    }
}
