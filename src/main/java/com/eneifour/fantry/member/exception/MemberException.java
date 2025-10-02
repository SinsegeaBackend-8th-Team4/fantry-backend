package com.eneifour.fantry.member.exception;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {
    private final MemberErrorCode memberCode;

    public MemberException(MemberErrorCode memberCode) {
        super(memberCode.getMessage());
        this.memberCode = memberCode;
    }

    public MemberException(MemberErrorCode memberCode, Throwable cause) {
        super(memberCode.getMessage(), cause);
        this.memberCode = memberCode;
    }
}
