package com.eneifour.fantry.settlement.scheduler;

import com.eneifour.fantry.settlement.service.SettlementAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 정산 자동 실행을 위한 스케줄러.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SettlementScheduler {

    private final SettlementAdminService settlementAdminService;

    /**
     * 매일 새벽 3시에 정산 로직을 실행합니다.
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void runDailySettlement() {
        log.info("Starting daily settlement process for date: {}", LocalDate.now());
        try {
            settlementAdminService.executeSettlement(LocalDate.now());
            log.info("Successfully completed daily settlement process.");
        } catch (Exception e) {
            log.error("Error occurred during daily settlement process: {}", e.getMessage(), e);
        }
    }
}
