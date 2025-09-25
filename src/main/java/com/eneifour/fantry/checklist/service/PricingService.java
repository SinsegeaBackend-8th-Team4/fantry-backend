package com.eneifour.fantry.checklist.service;

import com.eneifour.fantry.checklist.repository.PriceBaselineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PricingService {
    private final PriceBaselineRepository priceBaselineRepository;

    // 카테고리별 최신 기준가 조회
    public Double getLatestBaselineAmount(int categoryId) {
        return priceBaselineRepository.findTopAmount(categoryId, LocalDateTime.now()).orElse(null);
    }
}
