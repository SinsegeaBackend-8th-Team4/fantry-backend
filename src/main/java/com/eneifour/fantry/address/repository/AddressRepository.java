package com.eneifour.fantry.address.repository;

import com.eneifour.fantry.address.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    public List<Address> findByMember_MemberId(int memberId);
    public Address findByAddressId(int addressId);
    // 기본 배송지(한 건) 조회 - isDefault는 '1' 또는 '0'로 저장되어 있음
    public Address findByMember_MemberIdAndIsDefault(int memberId, char isDefault);
}
