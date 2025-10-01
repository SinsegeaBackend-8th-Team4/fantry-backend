package com.eneifour.fantry.payment.domain.bootpay;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import java.time.ZonedDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VirtualBankDataDto {
    @JsonAlias("tid")
    private String tid;
    @JsonAlias("bank_code")
    private String bankCode;
    @JsonAlias("bank_name")
    private String bankName;
    @JsonAlias("bank_account")
    private String bankAccount;
    @JsonAlias("bank_username")
    private String bankUsername;
    @JsonAlias("expired_at")
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE_UTC)
    private ZonedDateTime expiredAt;
    @JsonAlias("sender_name")
    private String senderName;
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
