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

    // 특정 아티스트의 앨범 전제 조회 (발매일 내림차순)
    @Transactional(readOnly = true)
    public List<AlbumDto> getAllAlbumByArtis(Integer artistId){
        return albumRepository.findAlbumsByArtistArtistIdOrderByReleaseDateDesc(artistId)
                .stream()
                .map(AlbumDto::from)
                .toList();
    }
}