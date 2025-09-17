package com.eneifour.fantry.catalog.controller;

import com.eneifour.fantry.catalog.dto.AlbumDto;
import com.eneifour.fantry.catalog.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/albums")
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;

    // 특정 아티스의 앨범 전제 조회 (발매일 내림차순)
    public List<AlbumDto> getAlbumsByArtist(Integer artistId) {
        return albumService.getAllAlbumByArtis(artistId);
    }
}
