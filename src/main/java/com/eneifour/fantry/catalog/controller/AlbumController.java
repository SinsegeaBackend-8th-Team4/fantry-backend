package com.eneifour.fantry.catalog.controller;

import com.eneifour.fantry.catalog.dto.AlbumDto;
import com.eneifour.fantry.catalog.service.AlbumService;
import com.eneifour.fantry.inspection.support.api.InspectionApiResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/catalog/albums")
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;

    // 특정 아티스트의 앨범 전체 조회 (발매일 내림차순)
    @GetMapping
    public InspectionApiResponse<List<AlbumDto>> getAlbumsByArtist(
            @RequestParam @NotNull @Positive Integer artistId) {
        List<AlbumDto> albums = albumService.getAllAlbumByArtist(artistId);
        return InspectionApiResponse.ok(albums);
    }
}
