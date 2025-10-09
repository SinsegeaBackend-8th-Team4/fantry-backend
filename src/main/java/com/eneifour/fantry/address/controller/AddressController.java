package com.eneifour.fantry.address.controller;

import com.eneifour.fantry.address.dto.AddressRequest;
import com.eneifour.fantry.address.dto.AddressResponse;
import com.eneifour.fantry.address.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/address")
public class AddressController {
    private final AddressService addressService;

    //모든 배송지 내역 가져오기
    @GetMapping("/")
    public ResponseEntity<?> getAddresses() {
        List<AddressResponse> addresses = addressService.getAddresses();
        return ResponseEntity.ok().body(Map.of("addressList", addresses));
    }

    //한명의 회원에 대한 모든 주소 가져오기
    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getAddressMember(@PathVariable int memberId) {
        List<AddressResponse> addresses = addressService.getAddressesByMember(memberId);
        return ResponseEntity.ok().body(Map.of("addressList", addresses));
    }

    //하나의 배송지 가져오기
    @GetMapping("/{addressId}")
    public ResponseEntity<?> getAddress(@PathVariable int addressId) {
        AddressResponse address = addressService.getAddress(addressId);
        return ResponseEntity.ok().body(Map.of("address", address));
    }

    //배송지 추가하기
    @PostMapping("/")
    public ResponseEntity<?> createAddress(@RequestBody AddressRequest address) {
        addressService.saveAddress(address);
        return ResponseEntity.ok().body(Map.of("result", "주소가 성공적으로 등록됨"));
    }

    //배송지 수정하기
    @PutMapping("/{addressId}")
    public ResponseEntity<?> updateAddress(@PathVariable int addressId, @RequestBody AddressRequest address) {
        addressService.updateAddress(addressId, address);
        return ResponseEntity.ok().body(Map.of("result", "주소 내역이 성공적으로 수정됨"));
    }

    //배송지 삭제하기
    @DeleteMapping("/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable int addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.ok().body(Map.of("result", "주소가 성공적으로 삭제됨"));
    }
}
