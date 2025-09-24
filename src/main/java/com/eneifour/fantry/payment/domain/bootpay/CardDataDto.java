package com.eneifour.fantry.payment.domain.bootpay;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardDataDto {
    @JsonAlias("tid")
    private String tid;
    @JsonAlias("card_approve_no")
    private String cardApproveNo;
    @JsonAlias("card_no")
    private String cardNo;
    @JsonAlias("card_quota")
    private String cardQuota;
    @JsonAlias("card_company_code")
    private String cardCompanyCode;
    @JsonAlias("card_company")
    private String cardCompany;
    @JsonAlias("card_interest")
    private String cardInterest;
    @JsonAlias("card_type")
    private String cardType;
    @JsonAlias("receipt_url")
    private String receiptUrl;
    @JsonAlias("card_owner_type")
    private String cardOwnerType;
    @JsonAlias("point")
    private String point;
    @JsonAlias("cancelled_point")
    private String cancelledPoint;
    @JsonAlias("coupon")
    private String coupon;
    @JsonAlias("cancelled_coupon")
    private String cancelledCoupon;
    @JsonAlias("cancel_tid")
    private String cancelTid;
}
