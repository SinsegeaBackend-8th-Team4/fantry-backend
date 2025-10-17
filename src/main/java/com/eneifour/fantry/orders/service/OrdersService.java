package com.eneifour.fantry.orders.service;

import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.auction.exception.AuctionException;
import com.eneifour.fantry.auction.exception.ErrorCode;
import com.eneifour.fantry.auction.repository.AuctionRepository;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.repository.MemberRepository;
import com.eneifour.fantry.orders.domain.OrderStatus;
import com.eneifour.fantry.orders.domain.Orders;
import com.eneifour.fantry.orders.dto.OrdersRequest;
import com.eneifour.fantry.orders.dto.OrdersResponse;
import com.eneifour.fantry.orders.exception.OrdersException;
import com.eneifour.fantry.orders.repository.OrdersRepository;
import com.eneifour.fantry.payment.domain.Payment;
import com.eneifour.fantry.payment.domain.PaymentErrorCode;
import com.eneifour.fantry.payment.exception.PaymentException;
import com.eneifour.fantry.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final AuctionRepository auctionRepository;
    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;


    // =============================================
    // 1. 주문 조회 (Read)
    // =============================================

    //전체 Orders List 조회
    // paging 처리
    // 파라미터에 status 및 member 여부에 따라 해당 Record 조회 , 없다면 전체 조회
    @Transactional
    public Page<OrdersResponse> searchOrders(Pageable pageable, Integer memberId,OrderStatus orderStatus) {

        Page<Orders> ordersList;

        if(memberId != null && orderStatus != null){
            ordersList = ordersRepository.findByMember_MemberIdAndOrderStatus(pageable,memberId, orderStatus);
        }
        else if(orderStatus != null){
            ordersList = ordersRepository.findByOrderStatus(pageable,orderStatus);
        }else if(memberId != null){
            ordersList = ordersRepository.findByMember_MemberId(pageable,memberId);
        } else{
            ordersList = ordersRepository.findAll(pageable);
        }

        return ordersList.map(orders -> {
            OrdersResponse ordersResponse  = OrdersResponse.from(orders);
            if(orders.getShippingAddress() != null) {
                ordersResponse.setShippingAddress(orders.getShippingAddress());
            }

            if (orders.getDeliveredAt() != null) {
                ordersResponse.setDeliveredAt(orders.getDeliveredAt());
            }

            if (orders.getCancelledAt() != null) {
                ordersResponse.setCancelledAt(orders.getCancelledAt());
            }

            if (orders.getPayment() != null) {
                ordersResponse.setPaidAt(orders.getPayment().getPurchasedAt());
            }

            return ordersResponse;
        });
    }

    //주문 단건 조회
    @Transactional
    public OrdersResponse findOne(int ordersId) {

        Orders orders =  ordersRepository.findOrderDetailByOrdersId(ordersId)
                .orElseThrow(() -> new OrdersException(ErrorCode.ORDER_NOT_FOUND));

        OrdersResponse ordersResponse  = OrdersResponse.from(orders);

        if(orders.getShippingAddress() != null) {
            ordersResponse.setShippingAddress(orders.getShippingAddress());
        }

        if (orders.getDeliveredAt() != null) {
            ordersResponse.setDeliveredAt(orders.getDeliveredAt());
        }

        if (orders.getCancelledAt() != null) {
            ordersResponse.setCancelledAt(orders.getCancelledAt());
        }

        if (orders.getPayment() != null) {
            ordersResponse.setPaidAt(orders.getPayment().getPurchasedAt());
        }


        return ordersResponse;
    }

    //auction_id 와 일치하는 주문 단건 조회
    public OrdersResponse findByAuctionId(int auctionId){
        Orders orders = ordersRepository.findByAuction_AuctionId(auctionId)
                .orElseThrow(() ->new OrdersException(ErrorCode.ORDER_NOT_FOUND) );

        OrdersResponse ordersResponse  = OrdersResponse.from(orders);

        if(orders.getShippingAddress() != null) {
            ordersResponse.setShippingAddress(orders.getShippingAddress());
        }

        if (orders.getDeliveredAt() != null) {
            ordersResponse.setDeliveredAt(orders.getDeliveredAt());
        }

        if (orders.getCancelledAt() != null) {
            ordersResponse.setCancelledAt(orders.getCancelledAt());
        }

        if (orders.getPayment() != null) {
            ordersResponse.setPaidAt(orders.getPayment().getPurchasedAt());
        }

        return ordersResponse;
    }

    // =============================================
    // 2. 주문 생성/삭제 (Write)
    // =============================================

    //일반 판매 상품 1건 구매 및 결제 완료
    @Transactional
    public OrdersResponse createInstantBuyOrder(OrdersRequest ordersRequest) {
        Auction auction = auctionRepository.findById(ordersRequest.getAuctionId())
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));
        Member buyer = memberRepository.findById(ordersRequest.getBuyerId())
                .orElseThrow(() -> new OrdersException(ErrorCode.MEMBER_NOT_FOUND));
        Payment payment = paymentRepository.findByPaymentId(ordersRequest.getPaymentId())
                .orElseThrow(()-> new PaymentException(PaymentErrorCode.PAYMENT_NOT_FOUND));

        //DTO와 조회된 엔티티를 바탕으로 Orders 엔티티 생성

        Orders orders = Orders.builder()
                .auction(auction)
                .member(buyer)
                .price(ordersRequest.getPrice())
                .payment(payment)
                .orderStatus(OrderStatus.PAID)
                .build();

        Orders savedOrders = ordersRepository.save(orders);

        return findOne(savedOrders.getOrdersId());
    }

    // =============================================
    // 3. 주문 상태 변경 (Update)
    // =============================================

    //낙찰된 주문건 결제 완료 처리
    @Transactional
    public void completeAuctionPayment(String shippingAddress, int ordersId , int paymentId) {
        Orders order = ordersRepository.findById(ordersId)
                .orElseThrow(() -> new OrdersException(ErrorCode.ORDER_NOT_FOUND));

        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(()-> new PaymentException(PaymentErrorCode.PAYMENT_NOT_FOUND));

        order.completePayment(shippingAddress, payment);
    }

    //배송 준비중 처리
    @Transactional
    public void prepareShipment(int ordersId) {
        Orders order = ordersRepository.findById(ordersId)
                .orElseThrow(() -> new OrdersException(ErrorCode.ORDER_NOT_FOUND));
        order.prepareShipment();
    }

    //배송중 처리
    @Transactional
    public void ship(int ordersId) {
        Orders order = ordersRepository.findById(ordersId)
                .orElseThrow(() -> new OrdersException(ErrorCode.ORDER_NOT_FOUND));
        order.ship();
    }

    //배송 완료 처리
    @Transactional
    public void markAsDelivered(int ordersId) {
        Orders order = ordersRepository.findById(ordersId)
                .orElseThrow(() -> new OrdersException(ErrorCode.ORDER_NOT_FOUND));
        order.markAsDelivered();
    }

    //구매 확정 처리
    @Transactional
    public void confirmPurchase(int ordersId) {
        Orders order = ordersRepository.findById(ordersId)
                .orElseThrow(() -> new OrdersException(ErrorCode.ORDER_NOT_FOUND));
        order.confirmPurchase();
    }

    //취소 요청 처리
    @Transactional
    public void requestCancel(int ordersId) {
        Orders order = ordersRepository.findById(ordersId)
                .orElseThrow(() -> new OrdersException(ErrorCode.ORDER_NOT_FOUND));
        order.requestCancel();
    }

    //취소 완료 처리
    @Transactional
    public void cancel(int ordersId) {
        Orders order = ordersRepository.findById(ordersId)
                .orElseThrow(() -> new OrdersException(ErrorCode.ORDER_NOT_FOUND));
        order.cancel();
    }

    //환불 요청 처리
    @Transactional
    public void requestRefund(int ordersId) {
        Orders order = ordersRepository.findById(ordersId)
                .orElseThrow(() -> new OrdersException(ErrorCode.ORDER_NOT_FOUND));
        order.requestRefund();
    }

    //환불 완료 처리
    @Transactional
    public void completeRefund(int ordersId) {
        Orders order = ordersRepository.findById(ordersId)
                .orElseThrow(() -> new OrdersException(ErrorCode.ORDER_NOT_FOUND));
        order.completeRefund();
    }

}
