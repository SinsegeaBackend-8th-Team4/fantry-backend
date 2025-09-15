package com.eneifour.fantry.catalog.service;

import com.eneifour.fantry.catalog.dto.AlbumDto;
import com.eneifour.fantry.catalog.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;

    @Transactional(readOnly = true)
    public List<AlbumDto> getAlbumsByArtist(Long artistId) {
        return albumRepository.findByArtistArtistId(artistId)
                .stream()
                .map(AlbumDto::from)
                .toList();
    }
}
