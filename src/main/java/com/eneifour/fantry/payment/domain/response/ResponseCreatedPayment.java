package com.eneifour.fantry.payment.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseCreatedPayment {
    private String orderId;
}
