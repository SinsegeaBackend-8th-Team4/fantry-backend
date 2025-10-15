package com.eneifour.fantry.orders.dto;

import com.eneifour.fantry.orders.domain.Orders;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrdersResponse {
    // 주문 기본 정보
    private int ordersId;
    private int price;
    private String shippingAddress; // 결제완료되면 update
    private String orderStatus;
    private LocalDateTime orderedAt;
    private LocalDateTime paidAt; // 결제완료되면 update
    private LocalDateTime deliveredAt; // 배송 완료되면 update
    private LocalDateTime cancelledAt; // 주문 취소되면 update

    // 연관된 판매 정보
    private int auctionId;
    private String itemName;
    private String saleType;

    // 연관된 구매자 정보
    private int buyerId;
    private String buyerName;
    private String tel;

    //결제 정보
    private String receiptUrl; //결제 완료되면 update

    public static OrdersResponse from(Orders orders) {
        return OrdersResponse.builder()
                .ordersId(orders.getOrdersId())
                .price(orders.getPrice())
                .orderStatus(orders.getOrderStatus().toString())
                .orderedAt(orders.getCreatedAt())
                .auctionId(orders.getAuction().getAuctionId())
                .itemName(orders.getAuction().getProductInspection().getItemName())
                .saleType(orders.getAuction().getSaleType().toString())
                .buyerId(orders.getMember().getMemberId())
                .buyerName(orders.getMember().getName())
                .tel(orders.getMember().getTel())
                .build();
    }

}
