package com.eneifour.fantry.address.dto;

import com.eneifour.fantry.address.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {
    private int addressId;
    private String roadAddress;
    private String detailAddress;
    private String alias;
    private String recipientName;
    private String recipientTel;
    private char isDefault;
    private int memberId;

    public static AddressResponse from(Address address) {
        String[] addressParts = address.getDestinationAddress().split("##");
        String roadAddress = addressParts.length > 0 ? addressParts[0] : "";
        String detailAddress = addressParts.length > 1 ? addressParts[1] : "";

        return AddressResponse.builder()
                .addressId(address.getAddressId())
                .roadAddress(roadAddress)
                .detailAddress(detailAddress)
                .alias(address.getAlias())
                .recipientName(address.getRecipientName())
                .recipientTel(address.getRecipientTel())
                .isDefault(address.getIsDefault())
                .memberId(address.getMember().getMemberId())
                .build();
    }

    public static List<AddressResponse> fromList(List<Address> addresses) {
        return addresses.stream()
                .map(AddressResponse::from)
                .collect(Collectors.toList());
    }
}
