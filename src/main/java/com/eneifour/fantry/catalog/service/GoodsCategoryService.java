package com.eneifour.fantry.catalog.service;

import com.eneifour.fantry.catalog.dto.GoodsCategoryDto;
import com.eneifour.fantry.catalog.repository.GoodsCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsCategoryService {
    private final GoodsCategoryRepository goodsCategoryRepository;

    // 굿즈 카테고리 전체 조회
    public List<GoodsCategoryDto> getAllGategories() {
        return goodsCategoryRepository.findAll()
                .stream()
                .map(GoodsCategoryDto::from)
                .toList();
    }
}
