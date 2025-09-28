package com.eneifour.fantry.payment.domain.bootpay;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum BootpayError {
    //Access Token이 잘못됨
    TOKEN_KEY_INVALID("TOKEN_KEY_INVALID"),
    //REST API Application ID가 잘못된 값을 전달함
    APP_KEY_NOT_FOUND("APP_KEY_NOT_FOUND"),
    //REST API Application ID가 아닌 다른 플랫폼의 Application ID가 전달됨
    APP_KEY_NOT_REST("APP_KEY_NOT_REST"),
    //Private Key가 잘못됨
    APP_SK_NOT_MATCHED("APP_SK_NOT_MATCHED"),
    /*receipt_id를 찾을 수 없거나 혹은 조회 권한이 없음
     * - receipt_id가 없어 조회가 안되는 경우
     * - 발급 받은 access token으로 다른 프로젝트에서 결제된 receipt_id를 조회 혹은 결제 취소를 요청하는 경우
     */
    RC_NOT_FOUND("RC_NOT_FOUND"),
    /*파라메터 오류 혹은 PG사에서 결제 취소를 실패한 경우
     * - 휴대폰 소액결제의 경우 결제 당월 취소가 아니면 취소불가
     * - 부분취소시 부분취소가 안되는 가맹점인 경우
     * - 부분취소가 가능하더라도 일부 카드는 부분취소가 안됨 ( * 선불카드, 기프트 카드인 경우 )
     * - PG에서 정산받을 금액보다 취소 금액이 더 큰 경우 ( * PG사로 문의해서 입금 후 취소처리 )
     * - PG에서 가맹점에서 취소요청 대사 처리가 미승인인 경우 ( * PG사로 문의해서 해결 )
     * - 가상계좌 환불인 경우 PG사와 계약시 CMS 이체특약이 안되어있거나, 환불계좌/환불계좌주/환불은행 은행정보가 올바르지 않은 경우
     * - KCP인 경우 KCP 관리자에서 결제서버 IP로 부트페이 IP인, 223.130.82.4, 223.130.82.130, 223.130.82.131이 등록이 안된 경우
     */
    RC_CANCEL_SERVER_ERROR("RC_CANCEL_SERVER_ERROR"),
    /*부트페이서버에서 PG사로 취소요청 중 에러 발생된 케이스
     * - PG사 승인 서버 미응답 (timeout) 으로 인한 치명적인 에러
     * - DNS Resolve Timeout으로 인한 에러
     * - IDC 내 두 Router 간 네트워크 지연으로 인한 에러
     */
    RC_CANCEL_CRITICAL_ERROR("RC_CANCEL_CRITICAL_ERROR"),
    /*PG사에서 결제 승인중 발생된 에러
     * - 휴대폰 소액결제의 경우 소액결제 차단중이거나 잔여 소액결제 금액이 결제금액보다 작은 경우
     * - 카드 결제인 경우 만료된 카드/도난신고 카드/카드 잔액부족/카드 한도초과/카드사 점검으로 인한 에러
     * - 계좌이체의 경우 계좌 잔액부족으로 인한 에러
     */
    RC_CONFIRM_FAILED("RC_CONFIRM_FAILED"),
    /*부트페이서버에서 PG사로 승인요청 중 에러 발생된 케이스
     * - PG사 승인 서버 미응답 (timeout) 으로 인한 치명적인 에러
     * - DNS Resolve Timeout으로 인한 에러
     * - IDC 내 두 Router 간 네트워크 지연으로 인한 에러
     */
    RC_CONFIRM_CRITICAL_FAILED("RC_CONFIRM_CRITICAL_FAILED");


    private final String code;

    BootpayError(String code) {
        this.code = code;
    }

    private static final Map<String, BootpayError> BY_CODE =
            Arrays.stream(values()).collect(Collectors.toMap(BootpayError::getCode, Function.identity()));

    public static BootpayError fromCode(String code) {
        return BY_CODE.get(code);
    }
}
