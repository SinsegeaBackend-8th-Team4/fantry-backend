package com.eneifour.fantry.payment.service;

import com.eneifour.fantry.payment.domain.GhostPayment;
import com.eneifour.fantry.payment.domain.GhostPaymentStatus;
import com.eneifour.fantry.payment.domain.Payment;
import com.eneifour.fantry.payment.domain.bootpay.BootpayReceiptDto;
import com.eneifour.fantry.payment.exception.AlreadyCancelledPaymentException;
import com.eneifour.fantry.payment.exception.NotFoundPaymentException;
import com.eneifour.fantry.payment.mapper.PaymentMapper;
import com.eneifour.fantry.payment.repository.GhostPaymentRepository;
import com.eneifour.fantry.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 유령 결제(Ghost Payment) 처리 서비스입니다.
 * <p>
 * 유령 결제란 동시성 문제(Race Condition)로 인해 중복 처리된 결제를 의미합니다.
 * 이 서비스는 주기적으로 유령 결제를 감지하고 자동으로 취소 처리합니다.
 * </p>
 *
 * <p>
 * 스케줄링을 통해 1분마다 미처리된 유령 결제를 조회하여 Bootpay에 취소 요청을 보내고,
 * 로컬 DB의 결제 상태를 업데이트합니다.
 * </p>
 *
 * @see GhostPayment
 * @see GhostPaymentStatus
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GhostPaymentService {
    private final GhostPaymentRepository ghostPaymentRepository;
    private final CleanupExecutor cleanupExecutor;

    /**
     * 유령 결제를 주기적으로 정리하는 스케줄러입니다.
     * <p>
     * 1분(60,000ms)마다 실행되며, 취소되지 않은 모든 유령 결제를 조회하여
     * CleanupExecutor를 통해 취소 처리를 시도합니다.
     * </p>
     * <p>
     * 개별 유령 결제 처리 실패 시에도 스케줄러는 계속 실행됩니다.
     * </p>
     */
    @Scheduled(fixedDelay = 60000)
    @Async
    public void cleanupGhostPayments() {
        log.info("Start GhostPayment Cleanup");
        List<GhostPayment> ghostPayments = ghostPaymentRepository.findByStatusNot(GhostPaymentStatus.CANCEL_SUCCESS);
        for (GhostPayment ghostPayment : ghostPayments) {
            try {
                cleanupExecutor.execute(ghostPayment);
            } catch (Exception e) {
                log.error("유령 결제 처리 중 예상치 못한 최상위 오류 발생. 스케줄러는 계속됩니다. GhostPayment ID: {}", ghostPayment.getGhostPaymentId(), e);
            }
        }
        log.info("End GhostPayment Cleanup");
    }

    /**
     * 새로운 유령 결제 레코드를 생성합니다.
     * <p>
     * 동시성 문제로 인한 중복 결제가 감지되면 호출되며,
     * 새로운 트랜잭션(REQUIRES_NEW)으로 유령 결제를 저장하여
     * 메인 트랜잭션 롤백과 무관하게 기록을 보존합니다.
     * </p>
     *
     * @param receiptId 유령 결제로 기록할 Bootpay 영수증 ID
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createGhostPayment(String receiptId) {
        GhostPayment ghostPayment = new GhostPayment();
        ghostPayment.setReceiptId(receiptId);
        ghostPayment.setStatus(GhostPaymentStatus.CANCEL_RESERVATION);
        ghostPaymentRepository.save(ghostPayment);
    }

    /**
     * 유령 결제 정리 작업을 실행하는 내부 컴포넌트입니다.
     * <p>
     * GhostPaymentService와 별도 트랜잭션 관리를 위해 static inner class로 분리되었습니다.
     * </p>
     */
    @Component
    static class CleanupExecutor {
        private final GhostPaymentRepository ghostPaymentRepository;
        private final PaymentRepository paymentRepository;
        private final BootpayService bootpayService;

        public CleanupExecutor(
                GhostPaymentRepository ghostPaymentRepository,
                PaymentRepository paymentRepository,
                BootpayService bootpayService
        ) {
            this.ghostPaymentRepository = ghostPaymentRepository;
            this.paymentRepository = paymentRepository;
            this.bootpayService = bootpayService;
        }

        /**
         * 유령 결제(GhostPayment)를 처리합니다.
         * Bootpay를 통해 결제를 취소하고, GhostPayment의 상태를 업데이트합니다.
         *
         * @param ghostPayment 처리할 유령 결제 객체
         */
        @Transactional
        public void execute(GhostPayment ghostPayment) {
            try {
                log.info("유령 결제 취소 시도. Receipt ID: {}", ghostPayment.getReceiptId());
                BootpayReceiptDto bootpayReceiptDto = bootpayService.getReceiptViaWebClient(ghostPayment.getReceiptId());
                String PREFIX = "ghost_";
                BootpayReceiptDto resultReceiptDto = bootpayService.cancellationViaWebClient(bootpayReceiptDto.getReceiptId(), "유령결제", "CleanupFromServer", PREFIX + bootpayReceiptDto.getOrderId(), String.valueOf(bootpayReceiptDto.getPrice()), null);
                ghostPayment.setStatus(GhostPaymentStatus.CANCEL_SUCCESS);
                Payment payment = paymentRepository.findByOrderId(resultReceiptDto.getOrderId())
                        .orElseThrow(NotFoundPaymentException::new);
                PaymentMapper.updateFromDto(payment, resultReceiptDto);
                paymentRepository.save(payment);
                log.info("유령 결제 취소 성공. Receipt ID: {}", ghostPayment.getReceiptId());
            } catch (AlreadyCancelledPaymentException e) {
                ghostPayment.setStatus(GhostPaymentStatus.CANCEL_SUCCESS);
            } catch (Exception e) {
                log.error("유령 결제 취소 중 오류 발생. Receipt ID: {}, Error: {}", ghostPayment.getReceiptId(), e.getMessage());
                ghostPayment.setStatus(GhostPaymentStatus.CANCEL_FAILED);
            }
            ghostPaymentRepository.save(ghostPayment);
        }
    }

}
