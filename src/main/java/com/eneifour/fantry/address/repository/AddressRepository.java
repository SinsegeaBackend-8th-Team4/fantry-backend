package com.eneifour.fantry.address.repository;

import com.eneifour.fantry.address.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    public List<Address> findAddressByMemberId(int memberId);
    public Address findById(int addressId);
}
