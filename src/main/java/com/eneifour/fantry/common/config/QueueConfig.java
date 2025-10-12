package com.eneifour.fantry.common.config;

import com.eneifour.fantry.bid.domain.Bid;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 애플리케이션 내에서 Bid 객체를 저장할 때 사용할 In-Memory Queue를 Bean으로 등록하는 설정 클래스.
 */
@Configuration
public class QueueConfig {
    /**
     * 입찰 성공 기록(Bid 엔티티)을 임시 저장할 스레드 안전 큐(Thread-safe Queue).
     * @return BlockingQueue<Bid>의 싱글톤 Bean
     */
    @Bean
    public BlockingQueue<Bid> bidLogQueue() {
        // Java의 동시성 패키지에서 제공하는 스레드에 안전한 큐를 사용합니다.
        // LinkedBlockingQueue는 용량 제한 없이 사용할 수 있는 가장 일반적인 선택입니다.
        return new LinkedBlockingQueue<>();
    }
}
