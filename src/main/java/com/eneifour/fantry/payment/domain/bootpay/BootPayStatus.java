package com.eneifour.fantry.payment.domain.bootpay;

import lombok.Getter;

@Getter
public enum BootPayStatus {
    // 실패 상태
    CASH_RECEIPT_CANCEL_FAILED(-61, "현금영수증 발행취소 실패"),
    CASH_RECEIPT_ISSUE_FAILED(-60, "현금영수증 발행 실패"),
    AUTO_BILLING_KEY_FAILED(-40, "자동결제 빌링키 발급 실패"),
    CLOSE_PAYMENT(-15,"사용자가 결제창을 종료"),
    AUTO_BILLING_KEY_CANCELLED(-11, "자동결제 빌링키 발급 취소"),
    PAYMENT_APPROVAL_FAILED(-2, "결제 승인실패"),
    VIRTUAL_ACCOUNT_CANCELLED(-3, "가상계좌 발급 취소"),
    PAYMENT_REQUEST_FAILED(-4, "결제 요청 실패"),

    // 진행 상태
    PAYMENT_WAITING(0, "결제 대기"),
    PAYMENT_COMPLETED(1, "결제완료"),
    PAYMENT_APPROVING(2, "결제승인중"),
    PG_APPROVAL_REQUEST(4, "PG 결제 승인 요청"),
    VIRTUAL_ACCOUNT_ISSUED(5, "가상계좌 발급완료 및 입금 대기"),

    // 완료 상태
    AUTO_BILLING_KEY_COMPLETED(11, "자동결제 빌링키 발급 완료"),
    IDENTITY_VERIFICATION_COMPLETED(12, "본인인증 완료"),
    PAYMENT_CANCELLED(20, "결제취소 완료"),

    // 빌링키 관련
    AUTO_BILLING_KEY_READY(40, "자동결제 빌링키 발급 준비"),
    AUTO_BILLING_KEY_BEFORE(41, "자동결제 빌링키 발급 이전"),
    AUTO_BILLING_KEY_SUCCESS(42, "자동결제 빌링키 발급 성공"),

    // 기타
    IDENTITY_VERIFICATION_READY(50, "본인인증 시작 준비"),
    CASH_RECEIPT_ISSUED(60, "현금영수증 발행 완료"),
    CASH_RECEIPT_CANCELLED(61, "현금영수증 발행 취소 완료");

    private final int code;
    private final String description;

    BootPayStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static BootPayStatus fromCode(int code) {
        for (BootPayStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown payment status code: " + code);
    }

    public boolean isFailure() {
        return code < 0;
    }

    public boolean isCompleted() {
        return code == 1 || code == 11 || code == 12 || code == 20 ||
                code == 42 || code == 60 || code == 61;
    }

    public boolean isInProgress() {
        return code == 0 || code == 2 || code == 4 || code == 5 ||
                code == 40 || code == 41 || code == 50;
    }
}