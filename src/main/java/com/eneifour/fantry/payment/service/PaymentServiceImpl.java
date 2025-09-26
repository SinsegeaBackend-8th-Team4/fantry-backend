package com.eneifour.fantry.payment.service;

import com.eneifour.fantry.payment.annotation.RequireBootpayToken;
import com.eneifour.fantry.payment.domain.GhostPayment;
import com.eneifour.fantry.payment.domain.GhostPaymentStatus;
import com.eneifour.fantry.payment.domain.Payment;
import com.eneifour.fantry.payment.domain.bootpay.BootpayReceiptDto;
import com.eneifour.fantry.payment.domain.bootpay.BootpayStatus;
import com.eneifour.fantry.payment.domain.request.RequestPaymentApprove;
import com.eneifour.fantry.payment.domain.request.RequestPaymentCancel;
import com.eneifour.fantry.payment.domain.request.RequestPaymentCreate;
import com.eneifour.fantry.payment.exception.*;
import com.eneifour.fantry.payment.mapper.PaymentMapper;
import com.eneifour.fantry.payment.repository.GhostPaymentRepository;
import com.eneifour.fantry.payment.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final GhostPaymentRepository ghostPaymentRepository;
    private final BootpayService bootpayService;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            GhostPaymentRepository ghostPaymentRepository,
            BootpayService bootpayService
    ) {
        this.paymentRepository = paymentRepository;
        this.ghostPaymentRepository = ghostPaymentRepository;
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
    @RequireBootpayToken
    public BootpayReceiptDto getPreReceipt(String receiptId) throws Exception {
        return bootpayService.getReceipt(receiptId);
    }

    @Override
    @Transactional
    public void handleWebhook(String webhookData) throws Exception {
        BootpayReceiptDto result = bootpayService.convertWebhook(webhookData);
        if (BootpayStatus.fromCode(result.getStatus()) == BootpayStatus.CLOSE_PAYMENT) {
            Optional<Payment> existedPayment = paymentRepository.findByOrderId(result.getOrderId());
            Payment payment = PaymentMapper.dtoToEntity(result);
            existedPayment.ifPresent(p -> {
                payment.setPaymentId(p.getPaymentId());
                paymentRepository.save(payment);
            });
        }
    }

    @Override
    @RequireBootpayToken
    @Transactional
    public void purchaseItem(RequestPaymentApprove requestPaymentApprove) throws Exception {
        BootpayReceiptDto confirmReceiptResult = null;
        try {
            Optional<Payment> result = paymentRepository.findByOrderId(requestPaymentApprove.getOrderId());
            Payment payment = result.orElseThrow(() -> new NotFoundPaymentException("거래를 찾을 수 없습니다."));

            if (payment.getStatus() != 100) {
                throw new ProductNotAvailableForSaleException("상품이 판매 가능한 상태가 아닙니다.");
            }

            String receiptId = requestPaymentApprove.getReceiptId();
            BootpayReceiptDto preReceiptResult = bootpayService.getReceipt(receiptId);
            log.info("preReceiptResult : {}", preReceiptResult);

            if (payment.getPrice() - preReceiptResult.getPrice() != 0) {
                throw new PaymentAmountMismatchException("결제 금액이 상이합니다.");
            }

            Optional<Payment> checkResult = paymentRepository.findByOrderId(requestPaymentApprove.getOrderId());
            Payment checkPayment = checkResult.orElseThrow(() -> new NotFoundPaymentException("거래를 찾을 수 없습니다."));
            if (checkPayment.getStatus() != 100) {
                throw new ProductNotAvailableForSaleException("상품이 판매 가능한 상태가 아닙니다.");
            }

            confirmReceiptResult = bootpayService.approve(receiptId);
            log.info("confirmReceiptResult : {}", confirmReceiptResult);

            Payment paidPayment = PaymentMapper.dtoToEntity(confirmReceiptResult);
            paidPayment.setPaymentId(payment.getPaymentId());
            paidPayment.setVersion(payment.getVersion());
            log.info("receiptPayment : {}", paidPayment);
            paymentRepository.save(paidPayment);
        } catch (ObjectOptimisticLockingFailureException e) {
            GhostPayment ghostPayment = new GhostPayment();
            ghostPayment.setReceiptId(confirmReceiptResult.getReceiptId());
            ghostPayment.setStatus(GhostPaymentStatus.CANCEL_RESERVATION);
            ghostPaymentRepository.save(ghostPayment);
        }
    }

    @Override
    @RequireBootpayToken
    @Transactional
    public void cancelPayment(RequestPaymentCancel requestPaymentCancel) throws Exception {
        Optional<Payment> resultPayment = paymentRepository.findByReceiptId(requestPaymentCancel.getReceiptId());
        Payment payment = resultPayment.orElseThrow(() -> new NotFoundReceiptException("거래정보가 존재하지 않습니다."));
        BootpayReceiptDto resultReceipt = bootpayService.getReceipt(payment.getReceiptId());
        int availableCancelPrice = resultReceipt.getPrice() - resultReceipt.getCancelledPrice();
        int requestCancelPrice = Integer.parseInt(requestPaymentCancel.getCancelPrice());
        if (requestCancelPrice > availableCancelPrice) {
            throw new CancellableAmountExceededException("요청하신 금액이 취소가능 금액보다 많습니다.");
        }
        bootpayService.cancellation(payment.getReceiptId(), requestPaymentCancel.getCancelReason(), requestPaymentCancel.getAdminId(), resultReceipt.getOrderId(), requestPaymentCancel.getCancelPrice(), requestPaymentCancel.getBankDataDto());
    }
}
