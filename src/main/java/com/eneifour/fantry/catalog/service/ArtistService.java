package com.eneifour.fantry.catalog.service;

import com.eneifour.fantry.catalog.dto.ArtistDto;
import com.eneifour.fantry.catalog.repository.ArtistRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;

    @Transactional()
    public List<ArtistDto> getAllArtists() {
        return artistRepository.findAllByOrderByNameKoAsc()
                .stream()
                .map(ArtistDto::from)
                .toList();
    }
}
