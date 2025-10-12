package com.eneifour.fantry.bid.scheduler;

import com.eneifour.fantry.bid.domain.Bid;
import com.eneifour.fantry.bid.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/*
* In-Memory Queue에 쌓이 ㄴ입찰 기록 (Bid)를
* 주기적으로 DB에 Batch Insert 하는 스케쥴러
* */

@Component
@RequiredArgsConstructor
@Slf4j
public class BidScheduler {

    private final BlockingQueue<Bid> bidLogQueue;
    private final BidRepository bidRepository;

    //입찰 시도시 Bid -> In-Memory Queue 저장 스케쥴러 로직
    // fixedDelay: 이전 작업이 끝난 시점으로부터 2000ms(2초) 후에 다시 실행
    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void flushBidLogToDB() {
        // 1. 큐가 비어있으면 아무 작업도 하지 않고 즉시 종료 (불필요한 DB 접근 방지)
        if (bidLogQueue.isEmpty()) {
            return;
        }

        // 2. 큐에 현재 쌓여있는 모든 데이터를 리스트로 한번에 옮겨 담기.
        // drainTo는 원자적으로 동작하여 안전함.
        List<Bid> bidsToSave = new ArrayList<>();
        bidLogQueue.drainTo(bidsToSave);

        // 3. 옮겨 담은 데이터가 있을 경우에만 DB에 저장
        if (!bidsToSave.isEmpty()) {
            try {
                log.info("Attempting to save {} bid logs to DB in a batch.", bidsToSave.size());

                // 4. JPA의 saveAll을 사용하여 배치 INSERT 실행
                // application.properties의 batch_size 설정 필수.
                bidRepository.saveAll(bidsToSave);

                log.info("Successfully saved {} bid logs.", bidsToSave.size());
            } catch (Exception e) {
                log.error("Failed to save bid logs to DB. Batch size: {}. Error: {}", bidsToSave.size(), e.getMessage());
                // [고려사항] 실패 시 이 데이터를 어떻게 처리할지 정책결정
                // (예: 다시 큐에 넣기, 파일로 로깅하기 등)
            }
        }
    }



}
