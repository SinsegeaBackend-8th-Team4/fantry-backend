package com.eneifour.fantry.catalog.repository;

import com.eneifour.fantry.catalog.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {
    // 특정 아티스의 앨범 전제 조회 (발매일 내림차순)
    List<Album> findAlbumsByArtistArtistIdOrderByReleaseDateDesc(Integer artistId);
}