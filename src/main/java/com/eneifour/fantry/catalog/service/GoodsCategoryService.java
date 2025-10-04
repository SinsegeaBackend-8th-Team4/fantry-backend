package com.eneifour.fantry.catalog.service;

import com.eneifour.fantry.catalog.dto.GoodsCategoryDto;
import com.eneifour.fantry.catalog.repository.GoodsCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 굿즈 카테고리 정보와 관련된 비즈니스 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoodsCategoryService {
    private final GoodsCategoryRepository goodsCategoryRepository;

    /**
     * 모든 굿즈 카테고리 목록을 조회
     * @return 모든 굿즈 카테고리 DTO 리스트
     */
    public List<GoodsCategoryDto> getAllCategories() {
        return goodsCategoryRepository.findAll()
                .stream()
                .map(GoodsCategoryDto::from)
                .toList();
    }
}
