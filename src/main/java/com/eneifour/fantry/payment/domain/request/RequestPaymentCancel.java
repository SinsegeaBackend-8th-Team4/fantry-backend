package com.eneifour.fantry.payment.domain.request;

import com.eneifour.fantry.payment.domain.bootpay.BankDataDto;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RequestPaymentCancel {
    @JsonAlias({"receipt_id", "receiptId"})
    @NotBlank(message = "고유 영수증 번호가 누락되었습니다.")
    private String receiptId;
    @JsonAlias({"cancel_price", "cancelPrice"})
    @Pattern(regexp = "^\\d{1,10}$", message = "취소 금액은 0 이하가 될 수 없습니다.")
    @NotBlank(message = "취소 금액이 누락되었습니다.")
    private String cancelPrice;
    @JsonAlias({"memberId", "member_id"})
    @NotBlank(message = "멤버 아이디가 누락되었습니다.")
    private String adminId;
    @JsonAlias({"cancel_reason", "cancelReason"})
    private String cancelReason;
    @JsonAlias({"bank_data","bankData"})
    private BankDataDto bankDataDto;
}
