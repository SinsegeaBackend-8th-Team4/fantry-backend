package com.eneifour.fantry.address.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRequest {
    private String roadAddress;     //도로명 주소
    private String detailAddress;   //상세 주소
    private String alias;
    private String recipientName;
    private String recipientTel;
    private char isDefault;
    private int memberId;

    //도로명 주소 + 상세주소를 ## 구분자로 합치기
    public String getDestinationAddress(){
        return roadAddress + "##" + detailAddress;
    }
}
