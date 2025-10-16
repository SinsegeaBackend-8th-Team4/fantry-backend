package com.eneifour.fantry.payment.dto;

import com.eneifour.fantry.payment.domain.bootpay.BankDataDto;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PaymentCancelRequest {
    @JsonAlias({"order_id", "orderId"})
    @NotBlank(message = "주문 번호가 누락되었습니다.")
    private String orderId;

    @JsonAlias({"cancel_price", "cancelPrice"})
    @Pattern(regexp = "^\\d{1,10}$", message = "취소 금액은 0 이하가 될 수 없습니다.")
    @NotBlank(message = "취소 금액이 누락되었습니다.")
    private String cancelPrice;

    @JsonAlias({"user_name", "username"})
    @NotBlank(message = "유저아이디가 누락되었습니다.")
    private String username;

    @JsonAlias({"cancel_reason", "cancelReason"})
    private String cancelReason;

    @JsonAlias({"bank_data", "bankData"})
    private BankDataDto bankDataDto;
}
