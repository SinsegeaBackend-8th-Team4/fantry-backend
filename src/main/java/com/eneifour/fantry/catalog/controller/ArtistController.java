package com.eneifour.fantry.catalog.controller;

import com.eneifour.fantry.catalog.dto.ArtistDto;
import com.eneifour.fantry.catalog.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/artists")
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistService artistService;

    // 아티스트 전체 조회 (한글 이름 오름차순: ㄱ~ㅎ)
    @GetMapping
    public List<ArtistDto> getArtists() {
        return artistService.getAllArtists();
    }
}
