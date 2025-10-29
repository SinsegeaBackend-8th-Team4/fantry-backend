package com.eneifour.fantry.catalog.repository;

import com.eneifour.fantry.catalog.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    // 아티스트 전체 조회 (한글 이름 오름차순: ㄱ~ㅎ)
    List<Artist> findArtistsByOrderByNameKoAsc();

    @Query("SELECT new map(" +
            "   count(a) as totalArtists, " +
            "   sum(case when a.status = com.eneifour.fantry.catalog.domain.ArtistStatus.PENDING then 1 else 0 end) as pendingArtists, " +
            "   sum(case when a.status = com.eneifour.fantry.catalog.domain.ArtistStatus.APPROVED then 1 else 0 end) as approvedArtists, " +
            "   sum(case when a.status = com.eneifour.fantry.catalog.domain.ArtistStatus.REJECTED then 1 else 0 end) as rejectedArtists) " +
            "FROM Artist a")
    Map<String, Long> countArtistsByStatus();
}