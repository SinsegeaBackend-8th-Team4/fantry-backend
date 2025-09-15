package com.eneifour.fantry.catalog.repository;

import com.eneifour.fantry.catalog.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    // 특정 아티스트의 앨범
    List<Album> findByArtistArtistId(Long ArtistId);
    // SELECT * FROM album WHERE artist_id = ?;

    // 특정 아티스트 앨범을 발매일 역순 정렬
    List<Album> findByArtistArtistIdOrderByReleaseDateDesc(Long ArtistId);
    // SELECT * FROM album WHERE artist_id = ? ORDER BY release_date DESC;
}
