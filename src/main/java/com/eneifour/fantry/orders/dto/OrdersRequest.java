package com.eneifour.fantry.orders.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersRequest {
    @NotNull(message = "경매 ID는 필수입니다.")
    private int auctionId;

    @NotNull(message = "구매자 ID는 필수입니다.")
    private int buyerId;

    @NotNull(message = "가격 정보는 필수입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private int price;

    @NotNull(message = "결제 정보는 필수입니다.")
    private int paymentId;
}

