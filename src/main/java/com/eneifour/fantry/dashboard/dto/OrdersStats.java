package com.eneifour.fantry.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrdersStats {
    private final long totalOrders;
    private final long pendingPaymentOrders;
    private final long paidOrders;
    private final long preparingShipmentOrders;
    private final long shippedOrders;
    private final long deliveredOrders;
    private final long confirmedOrders;
    private final long cancelRequestedOrders;
    private final long cancelledOrders;
    private final long refundRequestedOrders;
    private final long refundedOrders;
}
