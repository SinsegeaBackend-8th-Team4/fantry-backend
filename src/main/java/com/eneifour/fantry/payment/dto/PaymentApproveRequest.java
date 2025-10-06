package com.eneifour.fantry.payment.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PaymentApproveRequest {
    @JsonAlias("receiptData")
    private String receiptData;
}
