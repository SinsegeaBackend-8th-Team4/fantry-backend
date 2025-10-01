package com.eneifour.fantry.payment.service;

import com.eneifour.fantry.payment.domain.Payment;
import com.eneifour.fantry.payment.domain.bootpay.BootpayReceiptDto;
import com.eneifour.fantry.payment.domain.request.RequestPaymentApprove;
import com.eneifour.fantry.payment.domain.request.RequestPaymentCancel;
import com.eneifour.fantry.payment.domain.request.RequestPaymentCreate;
import com.eneifour.fantry.payment.exception.*;
import com.eneifour.fantry.payment.mapper.PaymentMapper;
import com.eneifour.fantry.payment.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final GhostPaymentService ghostPaymentService;
    private final BootpayService bootpayService;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            GhostPaymentService ghostPaymentService,
            BootpayService bootpayService
    ) {
        this.paymentRepository = paymentRepository;
        this.ghostPaymentService = ghostPaymentService;
        this.bootpayService = bootpayService;
    }

    @Override
    @Transactional
    public Payment createPayment(RequestPaymentCreate requestPaymentCreate) throws CreatePaymentFailedException {
        Payment payment;
        try {
            payment = PaymentMapper.requestToEntity(requestPaymentCreate);
            paymentRepository.save(payment);
        } catch (NoSuchAlgorithmException e) {
            throw new CreatePaymentFailedException(e);
        }
        return payment;
    }

    @Override
    public List<Payment> getPayment(Pageable pageable, boolean desanding) {
        Page<Payment> page;
        if (desanding) {
            page = paymentRepository.findAllDesc(pageable);
        } else {
            page = paymentRepository.findAllAsc(pageable);
        }
        return page.stream().toList();
    }

    @Override
    public List<Payment> getPayment(Integer integer, Pageable pageable) {
        return List.of();
    }

    @Override
    public BootpayReceiptDto getPreReceipt(String receiptId) throws Exception {
        return bootpayService.getReceiptViaWebClient(receiptId);
    }

    @Override
    @Transactional
    public void handleWebhook(String webhookData) throws Exception {
//        BootpayReceiptDto result = bootpayService.convertWebhook(webhookData);
//        if (PaymentStatus.fromCode(result.getStatus()) == PaymentStatus.CLOSE_PAYMENT) {
//            Optional<Payment> existedPayment = paymentRepository.findByOrderId(result.getOrderId());
//            Payment payment = PaymentMapper.dtoToEntity(result);
//            existedPayment.ifPresent(p -> {
//                payment.setPaymentId(p.getPaymentId());
//                paymentRepository.save(payment);
//            });
//        }
    }

    @Override
    @Transactional
    public void purchaseItem(RequestPaymentApprove requestPaymentApprove) {
        BootpayReceiptDto confirmReceiptResult = new BootpayReceiptDto();
        confirmReceiptResult.setReceiptId(requestPaymentApprove.getReceiptId());
        try {
            Payment payment = paymentRepository.findByOrderId(requestPaymentApprove.getOrderId())
                    .orElseThrow(NotFoundPaymentException::new);

            if (!payment.isPaymentWaiting()) {
                throw new ProductNotAvailableForSaleException();
            }

            String receiptId = requestPaymentApprove.getReceiptId();
            BootpayReceiptDto preReceiptResult = bootpayService.getReceiptViaWebClient(receiptId);
            payment.validateAmount(preReceiptResult.getPrice());
            PaymentMapper.updateFromDto(payment, preReceiptResult);
            paymentRepository.save(payment);
            log.info("preReceiptResult : {}", preReceiptResult);

            confirmReceiptResult = bootpayService.approveViaWebClient(receiptId);
            log.info("confirmReceiptResult : {}", confirmReceiptResult);

            PaymentMapper.updateFromDto(payment, confirmReceiptResult);
            log.info("receiptPayment : {}", payment);
            paymentRepository.save(payment);
        } catch (ObjectOptimisticLockingFailureException e) {
            if (confirmReceiptResult != null) {
                ghostPaymentService.createGhostPayment(confirmReceiptResult.getReceiptId());
            }
            throw new ConcurrentPaymentException(e);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof TimeoutException) {
                if (confirmReceiptResult != null) {
                    ghostPaymentService.createGhostPayment(confirmReceiptResult.getReceiptId());
                }
            }
            throw e;
        }
    }

    @Override
    @Transactional
    public void cancelPayment(RequestPaymentCancel requestPaymentCancel) {
        Payment payment = paymentRepository.findByReceiptId(requestPaymentCancel.getReceiptId())
                .orElseThrow(NotFoundReceiptException::new);
        BootpayReceiptDto resultReceipt = new BootpayReceiptDto();
        resultReceipt.setReceiptId(payment.getReceiptId());
        try {
            resultReceipt = bootpayService.getReceiptViaWebClient(payment.getReceiptId());
            int availableCancelPrice = resultReceipt.getPrice() - resultReceipt.getCancelledPrice();
            int requestCancelPrice = Integer.parseInt(requestPaymentCancel.getCancelPrice());
            if (requestCancelPrice > availableCancelPrice) {
                throw new CancellableAmountExceededException();
            }
            BootpayReceiptDto bootpayReceiptDto = bootpayService.cancellationViaWebClient(payment.getReceiptId(), requestPaymentCancel.getCancelReason(), requestPaymentCancel.getMemberId(), resultReceipt.getOrderId(), requestPaymentCancel.getCancelPrice(), requestPaymentCancel.getBankDataDto());
            PaymentMapper.updateFromDto(payment, bootpayReceiptDto);
            paymentRepository.save(payment);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof TimeoutException) {
                ghostPaymentService.createGhostPayment(resultReceipt.getReceiptId());
            }
            throw e;
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }
}
