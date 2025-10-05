package com.eneifour.fantry.inspection.dto;

import com.eneifour.fantry.inspection.domain.ProductInspection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * FE에서 1차 온라인 검수 신청 시 보내는 JSON 데이터를 받기 위한 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class InspectionRequestDto {
    private Integer goodsCategoryId;
    private Integer artistId;
    private Integer albumId;
    private String itemName;
    private String itemDescription;
    private String hashtags;
    private Map<String, String> answers;
    private BigDecimal expectedPrice;
    private BigDecimal marketAvgPrice;
    private BigDecimal sellerHopePrice;
    private String shippingAddress;
    private String shippingAddressDetail;
    private String bankName;
    private String bankAccount;

    /**
     * DTO 객체를 ProductInspection 엔티티로 변환하는 메서드
     * @param memberId 검수 신청한 회원 ID
     * @return ProductInspection 엔티티 객체
     */
    public ProductInspection toEntity(int memberId) {
        return ProductInspection.builder()
                .memberId(memberId)
                .goodsCategoryId(this.goodsCategoryId)
                .submissionUuid(UUID.randomUUID().toString())
                .artistId(this.artistId)
                .albumId(this.albumId)
                .itemName(this.itemName)
                .itemDescription(this.itemDescription)
                .hashtags(this.hashtags)
                .expectedPrice(this.expectedPrice)
                .marketAvgPrice(this.marketAvgPrice)
                .sellerHopePrice(this.sellerHopePrice)
                .shippingAddress(this.shippingAddress)
                .shippingAddressDetail(this.shippingAddressDetail)
                .bankName(this.bankName)
                .bankAccount(this.bankAccount)
                .inspectionStatus(ProductInspection.InspectionStatus.SUBMITTED)
                .submittedAt(LocalDateTime.now())
                .build();
    }
}
