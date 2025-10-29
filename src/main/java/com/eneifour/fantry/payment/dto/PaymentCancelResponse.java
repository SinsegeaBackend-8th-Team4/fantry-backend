package com.eneifour.fantry.payment.dto;

import com.eneifour.fantry.payment.domain.bootpay.BootpayReceiptDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCancelResponse {
    private String receiptId;
    private String orderId;
    private Integer cancelledPrice;
    private String status;
    private LocalDateTime cancelledAt;

    public static PaymentCancelResponse from(BootpayReceiptDto dto) {
        return PaymentCancelResponse.builder()
                .receiptId(dto.getReceiptId())
                .orderId(dto.getOrderId())
                .cancelledPrice(dto.getCancelledPrice())
                .status(String.valueOf(dto.getStatus()))
                .cancelledAt(dto.getCancelledAt().toLocalDateTime())
                .build();
    }
}
