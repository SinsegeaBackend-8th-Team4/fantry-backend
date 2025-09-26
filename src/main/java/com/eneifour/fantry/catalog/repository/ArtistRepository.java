package com.eneifour.fantry.catalog.repository;

import com.eneifour.fantry.catalog.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    // 아티스트 전체 조회 (한글 이름 오름차순: ㄱ~ㅎ)
    List<Artist> findArtistsByOrderByNameKoAsc();
}