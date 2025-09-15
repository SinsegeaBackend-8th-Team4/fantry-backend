package com.eneifour.fantry.catalog.repository;

import com.eneifour.fantry.catalog.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    List<Artist> findAllByOrderByNameKoAsc();
}
