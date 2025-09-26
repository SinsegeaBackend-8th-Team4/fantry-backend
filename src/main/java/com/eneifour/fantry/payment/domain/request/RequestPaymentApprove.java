package com.eneifour.fantry.payment.domain.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class RequestPaymentApprove {
    @JsonAlias({"order_id", "orderId"})
    @NotBlank(message = "주문 아이디가 누락 되었습니다.")
    private String orderId;
    @JsonAlias({"receipt_id", "receiptId"})
    @NotBlank(message = "고유 영수증 번호가 누락 되었습니다.")
    private String receiptId;
}
