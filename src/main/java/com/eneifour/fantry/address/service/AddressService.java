package com.eneifour.fantry.address.service;

import com.eneifour.fantry.address.domain.Address;
import com.eneifour.fantry.address.dto.AddressRequest;
import com.eneifour.fantry.address.dto.AddressResponse;
import com.eneifour.fantry.address.exception.AddressErrorCode;
import com.eneifour.fantry.address.exception.AddressException;
import com.eneifour.fantry.address.repository.AddressRepository;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.repository.JpaMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final JpaMemberRepository memberRepository;

    //모든 배송지 주소 가져오기
    public List<AddressResponse> getAddresses() {
        return AddressResponse.fromList(addressRepository.findAll());
    }

    //회원 한명의 모든 배송지 주소 가져오기
    public List<AddressResponse> getAddressesByMember(int memberId){
        return AddressResponse.fromList(addressRepository.findByMember_MemberId(memberId));
    }

    //하나의 특정 배송지 내역 가져오기
    public AddressResponse getAddress(int addressId){
        Address address = addressRepository.findByAddressId(addressId);
        if(address == null){
            throw new AddressException(AddressErrorCode.ADDRESS_NOT_FOUND);
        }
        return AddressResponse.from(address);
    }

    //배송지 추가하기
    @Transactional
    public void saveAddress(AddressRequest addressRequest){
        Member member = memberRepository.findByMemberId(addressRequest.getMemberId());
        if(member == null){
            throw new AddressException(AddressErrorCode.ADDRESS_NOT_FOUND_MEMBER);
        }
        Address address = Address.builder()
                .destinationAddress(addressRequest.getDestinationAddress()) // roadAddress + ## + detailAddress
                .alias(addressRequest.getAlias())
                .recipientName(addressRequest.getRecipientName())
                .recipientTel(addressRequest.getRecipientTel())
                .isDefault(addressRequest.getIsDefault())
                .member(member)
                .build();

        Address savedAddress = addressRepository.save(address);
        AddressResponse.from(savedAddress);
    }

    //배송지 수정하기
    @Transactional
    public void updateAddress(int addressId, AddressRequest addressRequest) throws AddressException {
        Address address = addressRepository.findByAddressId(addressId);
        if(address == null){
            throw new AddressException(AddressErrorCode.ADDRESS_NOT_FOUND);
        }

        Member member = memberRepository.findByMemberId(addressRequest.getMemberId());
        if(member == null){
            throw new AddressException(AddressErrorCode.ADDRESS_NOT_FOUND_MEMBER);
        }

        address.update(
                addressRequest.getDestinationAddress(),
                addressRequest.getAlias(),
                addressRequest.getRecipientName(),
                addressRequest.getRecipientTel(),
                addressRequest.getIsDefault(),
                member
        );
        AddressResponse.from(address);
    }

    //배송지 삭제하기
    @Transactional
    public void deleteAddress(int addressId) throws AddressException {
        if(!addressRepository.existsById(addressId)){
            throw new AddressException(AddressErrorCode.ADDRESS_NOT_FOUND);
        }
        addressRepository.deleteById(addressId);
    }
}
