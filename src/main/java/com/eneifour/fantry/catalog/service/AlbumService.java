package com.eneifour.fantry.catalog.service;

import com.eneifour.fantry.catalog.dto.AlbumDto;
import com.eneifour.fantry.catalog.exception.CatalogErrorCode;
import com.eneifour.fantry.catalog.repository.AlbumRepository;
import com.eneifour.fantry.catalog.repository.ArtistRepository;
import com.eneifour.fantry.inspection.support.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 앨범 정보와 관련된 비즈니스 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumService {
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;

    /**
     * 특정 아티스트의 모든 앨범 목록을 발매일 내림차순으로 조회
     * @param artistId 앨범을 조회할 아티스트 ID
     * @return 해당 아티스트의 앨범 DTO 리스트
     * @throws BusinessException 요청한 아티스트 ID가 존재하지 않을 경우 발생
     */
    public List<AlbumDto> getAllAlbumByArtist(Integer artistId){
        // 아티스트 존재 여부 검증
        artistRepository.findById(artistId).orElseThrow(()->new BusinessException(CatalogErrorCode.ARTIST_NOT_FOUND));

        return albumRepository.findAlbumsByArtistArtistIdOrderByReleaseDateDesc(artistId)
                .stream()
                .map(AlbumDto::from)
                .toList();
    }
}