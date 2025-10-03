package com.eneifour.fantry.address.model;

import com.eneifour.fantry.address.domain.Address;
import com.eneifour.fantry.address.exception.AddressErrorCode;
import com.eneifour.fantry.address.exception.AddressException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final JpaAddressRepository jpaAddressRepository;

    //모든 배송지 주소 가져오기
    public List<Address> getAddresses() {
        return jpaAddressRepository.findAll();
    }

    //회원 한명의 모든 배송지 주소 가져오기
    public List<Address> getAddressesByMember(int memberId){
        return jpaAddressRepository.findAddressByMemberId(memberId);
    }

    //하나의 특정 배송지 내역 가져오기
    public Address getAddress(int addressId){
        return jpaAddressRepository.findById(addressId);
    }

    //배송지 추가하기
    public void saveAddress(Address address){
        jpaAddressRepository.save(address);
    }

    //배송지 수정하기
    @Transactional
    public void updateAddress(Address address) throws AddressException {
        if(!jpaAddressRepository.existsById(address.getAddressId())){
            throw new AddressException(AddressErrorCode.ADDRESS_NOT_FOUND);
        }
        jpaAddressRepository.save(address);
    }

    //배송지 삭제하기
    @Transactional
    public void deleteAddress(int addressId) throws AddressException {
        if(!jpaAddressRepository.existsById(addressId)){
            throw new AddressException(AddressErrorCode.ADDRESS_NOT_FOUND);
        }
        jpaAddressRepository.deleteById(addressId);
    }
}
