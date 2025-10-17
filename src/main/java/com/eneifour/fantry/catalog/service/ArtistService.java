package com.eneifour.fantry.catalog.service;

import com.eneifour.fantry.catalog.domain.Artist;
import com.eneifour.fantry.catalog.domain.ArtistStatus;
import com.eneifour.fantry.catalog.dto.ArtistCreateRequest;
import com.eneifour.fantry.catalog.dto.ArtistResponse;
import com.eneifour.fantry.catalog.dto.ArtistUpdateRequest;
import com.eneifour.fantry.catalog.exception.CatalogErrorCode;
import com.eneifour.fantry.catalog.repository.ArtistRepository;
import com.eneifour.fantry.inspection.support.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 아티스트 정보와 관련된 비즈니스 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArtistService {
    private final ArtistRepository artistRepository;

    /**
     * 모든 아티스트 목록을 한글 이름(오름차순)으로 정렬하여 조회
      * @return 모든 아티스트의 DTO 리스트 (ㄱ~ㅎ 순)
     */    
    public List<ArtistResponse> getAllArtists() {
        return artistRepository.findArtistsByOrderByNameKoAsc()
                .stream()
                .map(ArtistResponse::from)
                .toList();
    }

    @Transactional
    public Artist createPendingArtist(ArtistCreateRequest request){
        Artist artist = Artist.builder()
                .nameKo(request.getNameKo())
                .nameEn(request.getNameEn())
                .groupType(request.getGroupType())
                .status(ArtistStatus.PENDING)
                .build();

        return artistRepository.save(artist);
    }

    /**
     * 신규 아티스트 등록
     * @param request 등록할 아티스트 정보
     * @return 생성된 아티스트 정보
     */
    @Transactional
    public ArtistResponse createArtist(ArtistCreateRequest request) {
        Artist savedArtist = artistRepository.save(request.toEntity());
        return ArtistResponse.from(savedArtist);
    }

    /**
     * 기존 아티스트 수정
     * @param artistId 수정할 아티스트 ID
     * @param request 수정할 아티스트 정보
     * @return 수정된 아티스트 정보
     */
    @Transactional
    public ArtistResponse updateArtist(int artistId, ArtistUpdateRequest request) {
        Artist artist = artistRepository.findById(artistId).orElseThrow(
                () -> new BusinessException(CatalogErrorCode.ARTIST_NOT_FOUND));

        artist.setNameKo(request.getNameKo());
        artist.setNameEn(request.getNameEn());
        artist.setGroupType(request.getGroupType());

        return ArtistResponse.from(artist);
    }

    /**
     * 아티스트 삭제
     * @param artistId 삭제할 아티스트 ID
     */
    @Transactional
    public void deleteArtist(int artistId) {
        if(!artistRepository.existsById(artistId)) throw new BusinessException(CatalogErrorCode.ARTIST_NOT_FOUND);

        artistRepository.deleteById(artistId);
    }
}