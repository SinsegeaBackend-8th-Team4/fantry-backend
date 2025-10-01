package com.eneifour.fantry.payment.domain.bootpay;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.Map;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BootpayReceiptDto {
    @JsonAlias("receipt_id")
    private String receiptId;
    @JsonAlias("order_id")
    private String orderId;
    @JsonAlias("price")
    private Integer price;
    @JsonAlias("tax_free")
    private Integer taxFree;
    @JsonAlias("cancelled_price")
    private Integer cancelledPrice;
    @JsonAlias("cancelled_tax_free")
    private Integer cancelledTaxFree;
    @JsonAlias("order_name")
    private String orderName;
    @JsonAlias("company_name")
    private String companyName;
    @JsonAlias("gateway_url")
    private String gatewayUrl;
    @JsonAlias("metadata")
    private Map<String, Object> metadata;
    @JsonAlias("sandbox")
    private String sandbox;
    @JsonAlias("pg")
    private String pg;
    @JsonAlias("method")
    private String method;
    @JsonAlias("method_symbol")
    private String methodSymbol;
    @JsonAlias("method_origin")
    private String methodOrigin;
    @JsonAlias("method_origin_symbol")
    private String methodOriginSymbol;
    @JsonAlias("currency")
    private String currency;
    @JsonAlias("receipt_url")
    private String receiptUrl;
    @JsonAlias("purchased_at")
    private ZonedDateTime purchasedAt;
    @JsonAlias("cancelled_at")
    private ZonedDateTime cancelledAt;
    @JsonAlias("requested_at")
    private ZonedDateTime requestedAt;
    @JsonAlias("escrow_status_locale")
    private String escrowStatusLocale;
    @JsonAlias("escrow_status")
    private String escrowStatus;
    @JsonAlias("status_locale")
    private String statusLocale;
    @JsonAlias("status")
    private Integer status;
    @JsonAlias("card_data")
    private CardDataDto cardDataDto;
    @JsonAlias("phone_data")
    private Map<String,Object> phoneData;
    @JsonAlias("bank_data")
    private BankDataDto bankDataDto;
    @JsonAlias("vbank_data")
    private VirtualBankDataDto virtualBankDataDto;
    @JsonAlias("naver_point_data")
    private PointDataDto naverPointData;
    @JsonAlias("kakao_money_data")
    private PointDataDto kakaoMoneyData;
    @JsonAlias("payco_point_data")
    private PointDataDto paycoPointData;
    @JsonAlias("toss_point_data")
    private PointDataDto tossPointData;
    @JsonAlias("webhook_type")
    private String webhookType;
}
