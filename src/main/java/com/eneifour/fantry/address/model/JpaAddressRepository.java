package com.eneifour.fantry.address.model;

import com.eneifour.fantry.address.domain.Address;
import com.eneifour.fantry.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaAddressRepository extends JpaRepository<Address, Integer> {

    public List<Address> findAddressByMemberId(int memberId);
    public Address findById(int addressId);
}
