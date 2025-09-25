package com.eneifour.fantry.catalog.service;

import com.eneifour.fantry.catalog.dto.ArtistDto;
import com.eneifour.fantry.catalog.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArtistService {
    private final ArtistRepository artistRepository;

    // 아티스트 전체 조회 (한글명 오름차순)
    public List<ArtistDto> getAllArtists() {
        return artistRepository.findArtistsByOrderByNameKoAsc()
                .stream()
                .map(ArtistDto::from)
                .toList();
    }
}