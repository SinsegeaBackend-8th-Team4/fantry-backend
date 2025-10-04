package com.eneifour.fantry.catalog.service;

import com.eneifour.fantry.catalog.dto.ArtistDto;
import com.eneifour.fantry.catalog.repository.ArtistRepository;
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
    public List<ArtistDto> getAllArtists() {
        return artistRepository.findArtistsByOrderByNameKoAsc()
                .stream()
                .map(ArtistDto::from)
                .toList();
    }
}