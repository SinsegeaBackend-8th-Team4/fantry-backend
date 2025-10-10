package com.eneifour.fantry.payment.dto;

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
    private String reason;
    private String status;
    private LocalDateTime cancelledAt;
}
