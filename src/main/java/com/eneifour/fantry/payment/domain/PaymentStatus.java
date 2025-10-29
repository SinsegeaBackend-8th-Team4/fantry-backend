package com.eneifour.fantry.payment.domain;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum PaymentStatus {
    VERIFYING("verifying"),
    COMPLETE("complete"),
    FORGED("forged"),
    RETURNED("return"),
    CANCELED("cancel");

    private final String code;

    PaymentStatus(String code) {
        this.code = code;
    }

    private static final Map<String, PaymentStatus> BY_CODE = Arrays.stream(values()).collect(Collectors.toMap(PaymentStatus::getCode, Function.identity()));

    public static PaymentStatus fromCode(String code) {
        return BY_CODE.get(code);
    }
}
