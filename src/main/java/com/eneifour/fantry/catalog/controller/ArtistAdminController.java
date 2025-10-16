package com.eneifour.fantry.catalog.controller;

import com.eneifour.fantry.catalog.dto.ArtistCreateRequest;
import com.eneifour.fantry.catalog.dto.ArtistResponse;
import com.eneifour.fantry.catalog.dto.ArtistUpdateRequest;
import com.eneifour.fantry.catalog.service.ArtistService;
import com.eneifour.fantry.inspection.support.api.InspectionApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/catalog/artists")
public class ArtistAdminController {
    private final ArtistService artistService;

    /**
     * 신규 아티스트 등록
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201 Created 상태 코드 반환
    public InspectionApiResponse<ArtistResponse> createArtist(@Valid @RequestBody ArtistCreateRequest request) {
        ArtistResponse artist = artistService.createArtist(request);
        return InspectionApiResponse.ok(artist);
    }

    /**
     * 기존 굿즈 카테고리 수정
     */
    @PutMapping("/{artistId}")
    public InspectionApiResponse<ArtistResponse> updateArtist(
            @PathVariable int artistId,
            @Valid @RequestBody ArtistUpdateRequest request ) {
        ArtistResponse artist = artistService.updateArtist(artistId, request);
        return InspectionApiResponse.ok(artist);
    }

    /**
     * 기존 굿즈 카테고리 삭제
     */
    @DeleteMapping("/{artistId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public InspectionApiResponse<Void> deleteArtist(@PathVariable int artistId) {
        artistService.deleteArtist(artistId);
        return InspectionApiResponse.ok(null);
    }
}
