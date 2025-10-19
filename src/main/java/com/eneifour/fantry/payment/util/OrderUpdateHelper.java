package com.eneifour.fantry.payment.util;

import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.auction.domain.SaleStatus;
import com.eneifour.fantry.auction.dto.AuctionDetailResponse;
import com.eneifour.fantry.auction.repository.AuctionRepository;
import com.eneifour.fantry.auction.service.AuctionService;
import com.eneifour.fantry.orders.domain.Orders;
import com.eneifour.fantry.orders.dto.OrdersRequest;
import com.eneifour.fantry.orders.repository.OrdersRepository;
import com.eneifour.fantry.orders.service.OrdersService;
import com.eneifour.fantry.payment.domain.Payment;
import com.eneifour.fantry.payment.domain.bootpay.BootpayReceiptDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderUpdateHelper {
    private final OrdersService ordersService;
    private final AuctionService auctionService;
    private final AuctionRepository auctionRepository;

    @Transactional
    public void purchase(Payment payment, BootpayReceiptDto bootpayReceiptDto) {
        if (bootpayReceiptDto.getMetadata() == null) {
            return;
        }
        Map<String, Object> metaData = bootpayReceiptDto.getMetadata();
        if (metaData.get("auctionInfo") == null) {
            return;
        }
        if (metaData.get("deliveryInfo") == null) {
            return;

        }
        if (metaData.get("userInfo") == null) {
            return;

        }
        Map<String, Object> auctionInfo = (Map<String, Object>) metaData.get("auctionInfo");
        Map<String, Object> shippingInfo = (Map<String, Object>) metaData.get("deliveryInfo");
        Map<String, Object> userInfo = (Map<String, Object>) metaData.get("userInfo");
        if (auctionInfo.get("auctionId") == null) {
            return;
        }
        AuctionDetailResponse auction = auctionService.findByAuctionId((Integer) auctionInfo.get("auctionId"));
        String shippingAddress = shippingInfo.get("address") + " " + shippingInfo.get("detailAddress");
        if (auction.getSaleType().toLowerCase(Locale.ROOT).equals("auction")) {
            Orders orders = ordersService.findByAuctionId(auction.getAuctionId());

            log.info("ordersResponse : {}", orders);
            ordersService.completeAuctionPayment(shippingAddress, orders.getOrdersId(), payment.getPaymentId());
        } else {
            Orders orders = null;
            try {
                orders = ordersService.findByAuctionId(auction.getAuctionId());
            } catch (Exception ignore) {
            }
            if (orders != null) {
                return;
            }
            Optional<Auction> response = auctionRepository.findByIdForUpdate(auction.getAuctionId());
            response.ifPresent(value ->
            {
                if (value.getSaleStatus() == SaleStatus.SOLD) {
                    return;
                }

                if (value.getSaleStatus() == SaleStatus.ACTIVE || value.getSaleStatus() == SaleStatus.REACTIVE) {
                    value.closeAsSold((int) auctionInfo.get("itemPrice"));
                }
            });
            OrdersRequest ordersRequest = new OrdersRequest(auction.getAuctionId(), (int) userInfo.get("memberId"), (int) auctionInfo.get("itemPrice"), payment.getPaymentId(), shippingAddress);
            ordersService.createInstantBuyOrder(ordersRequest);
        }
    }

    @Transactional
    public void refund(BootpayReceiptDto bootpayReceiptDto) {
        Map<String, Object> metaData = bootpayReceiptDto.getMetadata();
        if (bootpayReceiptDto.getMetadata() == null) {
            return;
        }
        if (metaData.get("auctionInfo") == null) {
            return;
        }
        Map<String, Object> auctionInfo = (Map<String, Object>) metaData.get("auctionInfo");
        Orders orders = ordersService.findByAuctionId((int) auctionInfo.get("auctionId"));
        ordersService.completeRefund(orders.getOrdersId());
    }

    @Transactional
    public void cancel(BootpayReceiptDto bootpayReceiptDto) {
        try {
            Map<String, Object> metaData = bootpayReceiptDto.getMetadata();
            if (bootpayReceiptDto.getMetadata() == null) {
                return;
            }
            if (metaData.get("auctionInfo") == null) {
                return;
            }
            Map<String, Object> auctionInfo = (Map<String, Object>) metaData.get("auctionInfo");
            Orders orders = ordersService.findByAuctionId((int) auctionInfo.get("auctionId"));
            ordersService.cancel(orders.getOrdersId());
        }catch (Exception e) {
            log.error("주문 취소 에러 : {}", e.getMessage());
        }
    }
}
