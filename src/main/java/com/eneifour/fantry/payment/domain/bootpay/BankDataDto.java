package com.eneifour.fantry.payment.domain.bootpay;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankDataDto {
    @JsonAlias("tid")
    private String tid;
    @JsonAlias({"bank_code","bankCode"})
    private String bankCode;
    @JsonAlias("bank_name")
    private String bankName;
    @JsonAlias({"bank_account","bankAccount"})
    private String bankAccount;
    @JsonAlias({"bank_username","bankUsername"})
    private String bankUsername;
    @JsonAlias("cash_receipt_tid")
    private String cashReceiptTid;
    @JsonAlias("cash_receipt_type")
    private String cashReceiptType;
    @JsonAlias("cash_receipt_no")
    private String cashReceiptNo;
    @JsonAlias("receipt_url")
    private String receiptUrl;
    @JsonAlias("cash_receipt_url")
    private String cashReceiptUrl;
}
