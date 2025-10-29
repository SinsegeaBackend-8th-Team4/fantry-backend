package com.eneifour.fantry.payment.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreateRequest {
    @NotBlank(message = "유저아이디가 누락되었습니다.")
    @JsonAlias({"user_name", "username"})
    private String username;

    @Pattern(regexp = "^\\d{1,10}$", message = "잘못된 아이템 아이디 입니다.")
    @NotBlank(message = "아이템 아이디가 누락되었습니다.")
    @JsonAlias({"item_id", "itemId"})
    private String itemId;

    @Pattern(regexp = "^\\d{1,10}$", message = "결제금액은 0이하가 될 수 없습니다.")
    @NotBlank(message = "결제금액이 누락되었습니다.")
    @JsonAlias({"price"})
    private String price;
}
