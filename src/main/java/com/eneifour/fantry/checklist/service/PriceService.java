package com.eneifour.fantry.checklist.service;

import com.eneifour.fantry.checklist.domain.PriceBaseline;
import com.eneifour.fantry.checklist.repository.PriceBaselineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceService {
    private final PriceBaselineRepository priceBaselineRepository;

    // 카테고리별 최신 기준가 조회
    public Double getLatestBaselineAmount(int categoryId) {
        List<PriceBaseline> baselines = priceBaselineRepository.findLatestBaselineByCategoryId(categoryId);
        return baselines.isEmpty() ? null : baselines.getFirst().getAmount();
    }
}
