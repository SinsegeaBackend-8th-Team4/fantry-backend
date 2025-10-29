package com.eneifour.fantry.payment.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentVerifyRequest {
    @JsonAlias({"receiptId", "receipt_id"})
    private String receiptId;
    @JsonAlias({"paymentStatus", "payment_status"})
    private String paymentStatus;
}
