package com.eneifour.fantry.address.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AddressErrorCode {
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "AD001", "존재하지 않는 주소입니다."),
    ADDRESS_NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "AD002", "배송지 등록/수정에 필요한 회원을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
