package com.eneifour.fantry.checklist.service;

import com.eneifour.fantry.checklist.dto.PriceBaselineDto;
import com.eneifour.fantry.checklist.repository.PriceBaselineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceService {
    private final PriceBaselineRepository priceBaselineRepository;

    public List<PriceBaselineDto> findWithCategoryByCategoryId(@Param("categoryId") int categoryId) {
        return priceBaselineRepository.findWithCategoryByCategoryId(categoryId).stream()
                .map(PriceBaselineDto::from)
                .toList();
    }
}
