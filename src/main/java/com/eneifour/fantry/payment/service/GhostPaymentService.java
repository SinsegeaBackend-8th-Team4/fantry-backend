package com.eneifour.fantry.payment.service;

import com.eneifour.fantry.payment.domain.GhostPayment;
import com.eneifour.fantry.payment.domain.GhostPaymentStatus;
import com.eneifour.fantry.payment.domain.Payment;
import com.eneifour.fantry.payment.domain.bootpay.BootpayReceiptDto;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class GhostPaymentService {
    private final GhostPaymentRepository ghostPaymentRepository;
    private final CleanupExecutor cleanupExecutor;

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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createGhostPayment(String receiptId) {
        GhostPayment ghostPayment = new GhostPayment();
        ghostPayment.setReceiptId(receiptId);
        ghostPayment.setStatus(GhostPaymentStatus.CANCEL_RESERVATION);
        ghostPaymentRepository.save(ghostPayment);
    }

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
                Payment payment = paymentRepository.findPaymentByOrderId(resultReceiptDto.getOrderId())
                        .orElseThrow(NotFoundPaymentException::new);
                PaymentMapper.updateFromDto(payment, resultReceiptDto);
                paymentRepository.save(payment);
                log.info("유령 결제 취소 성공. Receipt ID: {}", ghostPayment.getReceiptId());
            } catch (Exception e) {
                log.error("유령 결제 취소 중 오류 발생. Receipt ID: {}, Error: {}", ghostPayment.getReceiptId(), e.getMessage());
                ghostPayment.setStatus(GhostPaymentStatus.CANCEL_FAILED);
            }
            ghostPaymentRepository.save(ghostPayment);
        }
    }

}
