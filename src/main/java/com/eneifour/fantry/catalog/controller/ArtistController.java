package com.eneifour.fantry.catalog.controller;

import com.eneifour.fantry.catalog.dto.ArtistDto;
import com.eneifour.fantry.catalog.service.ArtistService;
import com.eneifour.fantry.inspection.support.api.InspectionApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/catalog/artists")
public class ArtistController {
    private final ArtistService artistService;

    // 아티스트 전체 조회 (한글 이름 오름차순: ㄱ~ㅎ)
    @GetMapping
    public InspectionApiResponse<List<ArtistDto>> getArtists() {
        List<ArtistDto> artists = artistService.getAllArtists();
        return InspectionApiResponse.ok(artists);
    }
}
